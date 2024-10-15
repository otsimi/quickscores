import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.live.quickscores.FixturesResponse
import com.live.quickscores.R
import com.live.quickscores.RAPID_API_KEY
import com.live.quickscores.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchFragment : Fragment() {

    private lateinit var selectedDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_match, container, false)

        arguments?.let {
            selectedDate = it.getString("selected_date").orEmpty()
        }

        fetchDataForDate(selectedDate)

        return view
    }

    private fun fetchDataForDate(date: String) {
        val apiKey= RAPID_API_KEY
        RetrofitClient.apiService.fetchFixtures(apiKey).enqueue(object :Callback<FixturesResponse>{
            override fun onResponse(p0: Call<FixturesResponse>, p1: Response<FixturesResponse>) {
                if (p1.isSuccessful){
                    val fixtures = p1.body()
                    // Handle the fetched fixtures here
                } else{
                    Log.e("API Error", p1.message())
                }
            }

            override fun onFailure(p0: Call<FixturesResponse>, p1: Throwable) {
                Log.e("API Failure", p1.message ?: "Unknown error")
            }

        })
    }
}
