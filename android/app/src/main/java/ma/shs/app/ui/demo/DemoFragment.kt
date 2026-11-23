package ma.shs.app.ui.demo

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ma.shs.app.R
import ma.shs.app.common.UiState
import ma.shs.app.common.toText
import ma.shs.app.databinding.FragmentDemoBinding

@AndroidEntryPoint
class DemoFragment : Fragment(R.layout.fragment_demo) {

    private val viewModel: DemoViewModel by viewModels()
    private var _binding: FragmentDemoBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentDemoBinding.bind(view)
        binding.btnContent.setOnClickListener { viewModel.refresh(DemoMode.CONTENT) }
        binding.btnEmpty.setOnClickListener { viewModel.refresh(DemoMode.EMPTY) }
        binding.btnError.setOnClickListener { viewModel.refresh(DemoMode.NETWORK_ERROR) }
        binding.btnRetry.setOnClickListener { viewModel.refresh(DemoMode.CONTENT) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { render(it) }
            }
        }
    }

    private fun render(state: UiState<List<String>>) {
        binding.progress.isVisible = state is UiState.Loading
        binding.tvMessage.isVisible = state !is UiState.Loading
        binding.btnRetry.isVisible = state is UiState.Error
        binding.tvMessage.text = when (state) {
            UiState.Loading -> ""
            is UiState.Content -> state.data.joinToString("\n")
            UiState.Empty -> getString(R.string.common_empty)
            is UiState.Error -> state.error.toText(requireContext())
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}