package com.live.quickscores.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.live.quickscores.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.quickscores.adapters.FavoritesAdapter
import com.live.quickscores.databinding.FragmentFavoritesBinding
import com.live.quickscores.viewmodelclasses.FavoriteViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class FavoritesFragment : Fragment()  {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesAdapter: FavoritesAdapter
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeFavorites()
        viewModel.refreshFavorites()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerView() {
        favoritesAdapter = FavoritesAdapter(onItemClick = { match ->
            Toast.makeText(
                requireContext(),
                "Clicked on: ${match.homeTeam} vs ${match.awayTeam}",
                Toast.LENGTH_SHORT
            ).show()
            val formattedDate=formatDate(match.time)
            Log.d("FavoritesAdapter", "Match period: $formattedDate")

            val args = Bundle().apply {
                putString("matchId", match.fixtureId.toString())
                putString("homeTeam", match.homeTeam)
                putString("awayTeam", match.awayTeam)
                putString("homeTeamLogoUrl", match.homeLogo)
                putString("awayTeamLogoUrl", match.awayLogo)
//                putString("leagueName", match.)
                putString("date", formattedDate)
                putString("homeTeamGoals", match.homeGoals)
                putString("awayTeamGoals", match.awayGoals)
                putString("venue",match.venue)
                putString("country",match.country)
                putString("referee",match.referee)
                putString("city",match.city)

                println("match time${match.time}")
            }



            findNavController().navigate(R.id.action_favoritesFragment_to_fixtureFragment, args)
        })

        binding.RecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoritesAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.favoriteFixtures.observe(viewLifecycleOwner) { favoritesList ->
            Log.d("FavoritesFragment", "Favorites list size: ${favoritesList.size}")
            favoritesAdapter.updateList(favoritesList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(dateString: String): String? {
        return try {
            val zonedDateTime = ZonedDateTime.parse(dateString)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            zonedDateTime.format(formatter)
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
            null
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoritesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
