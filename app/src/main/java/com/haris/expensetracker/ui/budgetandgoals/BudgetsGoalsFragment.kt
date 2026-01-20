package com.haris.expensetracker.ui.budgetandgoals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.haris.expensetracker.activities.CreateGoalActivity
import com.haris.expensetracker.activities.CreateNewBudgetActivity
import com.haris.expensetracker.adapter.BudgetAdapter
import com.haris.expensetracker.adapter.GoalAdapter
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.databinding.FragmentBudgetsGoalsBinding
import com.haris.expensetracker.room.AppDatabase
import com.haris.expensetracker.ui.budget.BudgetViewModel
import com.haris.expensetracker.ui.budget.BudgetViewModelFactory
import com.haris.expensetracker.ui.goal.GoalViewModel
import com.haris.expensetracker.ui.goal.GoalViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.haris.expensetracker.activities.AddGoalActivity

class BudgetsGoalsFragment : Fragment() {

    private var _binding: FragmentBudgetsGoalsBinding? = null
    private val binding get() = _binding!!
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var goalViewModel: GoalViewModel
    private lateinit var budgetAdapter: BudgetAdapter
    private lateinit var goalAdapter: GoalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetsGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val database = AppDatabase.getDatabase(requireContext())
        val repository = FinanceRepository(database.FinanceDao())
        val budgetFactory = BudgetViewModelFactory(repository)
        val goalFactory = GoalViewModelFactory(repository)
        budgetViewModel = ViewModelProvider(this, budgetFactory)[BudgetViewModel::class.java]
        goalViewModel = ViewModelProvider(this, goalFactory)[GoalViewModel::class.java]

        budgetAdapter = BudgetAdapter(
            onCreateClick = {
                startActivity(Intent(context, CreateNewBudgetActivity::class.java))
            },
            onDeleteClick = { budgetId ->
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Delete Budget")
                    .setMessage("Are you sure you want to delete this budget?")
                    .setPositiveButton("Yes") { _, _ ->
                        budgetViewModel.deleteBudget(budgetId)
                    }
                    .setNegativeButton("No", null)
                    .show()
            },
            onEditClick =  { budget ->
                val intent = Intent(context, CreateNewBudgetActivity::class.java)
                intent.putExtra("BUDGET_ID", budget.id)
                startActivity(intent)
            }
        )

        goalAdapter = GoalAdapter(
            onCreateClick = {
                startActivity(Intent(context, CreateGoalActivity::class.java))
            },
            onDeleteClick = { goalId ->
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Delete Goal")
                    .setMessage("Are you sure you want to delete this Goal?")
                    .setPositiveButton("Yes") { _, _ ->
                        goalViewModel.deleteGoal(goalId)
                    }
                    .setNegativeButton("No", null)
                    .show()
            },
            onEditClick = { goalId ->
                val intent = Intent(context, AddGoalActivity::class.java)
                intent.putExtra("GOAL_ID", goalId.id)
                startActivity(intent)
            }
        )

        binding.budgetRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.budgetRecyclerView.adapter = budgetAdapter

        binding.goalRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.goalRecyclerView.adapter = goalAdapter

        budgetViewModel.getBudgets(uid).observe(viewLifecycleOwner) { rawList ->
            if (rawList.isNullOrEmpty()) {
                binding.cardBudgetEmpty.visibility = View.VISIBLE
                binding.budgetRecyclerView.visibility = View.GONE
            } else {
                binding.cardBudgetEmpty.visibility = View.GONE
                binding.budgetRecyclerView.visibility = View.VISIBLE
                budgetViewModel.calculateBudgetProgress(rawList, uid)
            }
        }

        budgetViewModel.budgetsWithProgress.observe(viewLifecycleOwner) { calculatedList ->
            binding.cardBudgetEmpty.visibility = View.GONE
            binding.budgetRecyclerView.visibility = View.VISIBLE
            budgetAdapter.submitList(calculatedList)
        }

        goalViewModel.getGoals(uid).observe(viewLifecycleOwner) { goalList ->
            if (goalList.isNullOrEmpty()) {
                binding.cardGoalEmpty.visibility = View.VISIBLE
                binding.goalRecyclerView.visibility = View.GONE
            } else {
                binding.cardGoalEmpty.visibility = View.GONE
                binding.goalRecyclerView.visibility = View.VISIBLE
                goalViewModel.calculateGoalsProgress(goalList, uid)
            }
        }

        goalViewModel.goalsWithProgress.observe(viewLifecycleOwner) { calculatedList ->
            binding.cardGoalEmpty.visibility = View.GONE
            binding.goalRecyclerView.visibility = View.VISIBLE
            goalAdapter.submitList(calculatedList)
        }

        binding.createGoalBtn.setOnClickListener {
            startActivity(Intent(context, CreateGoalActivity::class.java))
        }

        binding.createBudgetBtn.setOnClickListener {
            startActivity(Intent(context, CreateNewBudgetActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}