package com.live.quickscores.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.live.quickscores.R
import com.live.quickscores.databinding.FragmentMatchInfoBinding


class MatchInfoFragment : Fragment() {
    private var infoBinding:FragmentMatchInfoBinding?=null
    private val binding get() = infoBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       infoBinding=FragmentMatchInfoBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            arguments.let {
                if (it != null) {
                    binding?.matchDate?.text=it.getString("date")
                    binding?.matchReferee?.text=it.getString("referee")
                    binding?.matchVenue?.text=it.getString("venue")
                    binding?.matchCity?.text=it.getString("city")
                    binding?.matchCountry?.text=it.getString("country")
                }


            }
        }


    companion object {

        fun newInstance() =
            MatchInfoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}