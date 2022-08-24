package com.udacity.asteroidradar.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.ui.adapters.AsteroidClickListener
import com.udacity.asteroidradar.ui.adapters.AsteroidsAdapter
import com.udacity.asteroidradar.utils.MainViewModelFactory
import com.udacity.asteroidradar.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMainBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        viewModel =
            ViewModelProviders.of(
                this,
                MainViewModelFactory(
                    AsteroidDataBase.getInstance(application),
                    application
                )
            ).get(MainViewModel::class.java)
        val adapter = AsteroidsAdapter(AsteroidClickListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })

        with(binding) {
            mainViewModel = viewModel
            lifecycleOwner = this@MainFragment
            root.asteroid_recycler.adapter = adapter
        }
        setUpObservers(viewModel, adapter)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_menu -> {
                viewModel.getWeekAsteroids()
            }
            R.id.show_today_menu -> {
                viewModel.getTodayAsteroids()
            }
            R.id.show_saved_menu -> {
                viewModel.getSavedAsteroids()
            }
        }
        return true
    }

    private fun setUpObservers(viewModel: MainViewModel, adapter: AsteroidsAdapter) {
        with(viewModel) {
            allAsteroids.observe(viewLifecycleOwner) {
                it?.let {
                    adapter.asteroidsList = it
                }
            }
            navigateToAsteroidDetails.observe(viewLifecycleOwner) { asteroid ->
                asteroid?.let {
                    findNavController().navigate(
                        MainFragmentDirections.actionShowDetail(
                            asteroid
                        )
                    )
                    onAsteroidDetailsNavigated()
                }
            }
        }
    }
}
