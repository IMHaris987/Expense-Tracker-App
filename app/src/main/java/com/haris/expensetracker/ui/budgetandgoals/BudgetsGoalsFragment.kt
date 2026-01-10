import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.haris.expensetracker.R
import com.haris.expensetracker.activities.CreateGoalActivity
import com.haris.expensetracker.activities.CreateNewBudgetActivity

class BudgetsGoalsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budgets_goals, container, false)

        val createBudgetButton = view.findViewById<TextView>(R.id.createBudgetBtn)
        val createGoalButton = view.findViewById<TextView>(R.id.createGoalBtn)

        createBudgetButton.setOnClickListener {
            startActivity(Intent(context, CreateNewBudgetActivity::class.java))
        }

        createGoalButton.setOnClickListener {
            startActivity(Intent(context, CreateGoalActivity::class.java))
        }
        return view
    }
}