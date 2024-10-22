package com.live.quickscores

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FixtureFragment : Fragment() {

    private var matchId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            matchId = it.getString("matchId")
            Log.d("FragmentTransaction", "Navigation completed: ${matchId}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("FragmentTransaction", "onCreateView in FixtureFragment called")
        return inflater.inflate(R.layout.fragment_fixture, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matchId?.let {
            fetchMatchDetails(it)
        }

    }
    private fun fetchMatchDetails(matchId: String){

    }

    companion object {
        fun newInstance(matchId: String) =
            FixtureFragment().apply {
                arguments = Bundle().apply {
                    putString("matchId", matchId)
                }
            }
    }
}