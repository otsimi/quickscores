package com.live.quickscores.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.quickscores.R
import com.live.quickscores.adapters.FavoritesAdapter
import com.live.quickscores.databinding.FragmentFavoritesBinding
import com.live.quickscores.viewmodelclasses.FavoriteViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesAdapter: FavoritesAdapter
    private val viewModel: FavoriteViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeFavorites()
        observeLiveFavorites()
        observeFavoriteIds()
        viewModel.refreshFavorites()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerView() {
        favoritesAdapter = FavoritesAdapter(onItemClick = { match,homeGoals,awayGoals ->
            Toast.makeText(
                requireContext(),
                "Clicked on: ${match.homeTeam} vs ${match.awayTeam}",
                Toast.LENGTH_SHORT
            ).show()

            val formattedDate = formatDate(match.time)
            Log.d("FavoritesFragment", "Match period: $formattedDate")

            val args = Bundle().apply {
                putString("matchId", match.fixtureId.toString())
                putString("homeTeam", match.homeTeam)
                putString("awayTeam", match.awayTeam)
                putString("homeTeamLogoUrl", match.homeLogo)
                putString("awayTeamLogoUrl", match.awayLogo)
                putString("date", formattedDate)
                putString("homeTeamGoals",homeGoals.toString())
                putString("awayTeamGoals", awayGoals.toString())
                putString("venue", match.venue)
                putString("country", match.country)
                putString("referee", match.referee)
                putString("city", match.city)
            }

            findNavController().navigate(
                R.id.action_favoritesFragment_to_fixtureFragment,
                args
            )
        })

        binding.RecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoritesAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.favoriteFixtures.observe(viewLifecycleOwner) { favoritesList ->
            Log.d("FavoritesFragment", "Favorites list size: ${favoritesList.size}")
            favoritesAdapter.updateFavorites(favoritesList)
        }
    }

    private fun observeLiveFavorites() {
        viewModel.favoriteLiveFixtures.observe(viewLifecycleOwner) { liveFixtures ->
            Log.d("FavoritesFragment", "Live fixtures received: ${liveFixtures.size}")
            favoritesAdapter.updateLiveStats(liveFixtures)
        }
    }

    private fun observeFavoriteIds() {
        viewModel.favoriteIds.observe(viewLifecycleOwner) { ids ->
            if (ids.isNotEmpty()) {
                viewModel.refreshLiveFavorites(ids.toList())
                Log.d("FavoritesFragment", "Favorite IDs: $ids")

            }
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
}
