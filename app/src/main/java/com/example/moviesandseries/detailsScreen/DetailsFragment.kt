package com.example.moviesandseries.detailsScreen

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.moviesandseries.R
import com.example.moviesandseries.activities.main.MainActivity
import com.example.moviesandseries.activities.main.MainViewModel
import com.example.moviesandseries.common.*
import com.example.moviesandseries.common.constants.Constants
import com.example.moviesandseries.common.enums.ShowType
import com.example.moviesandseries.common.helpers.hide
import com.example.moviesandseries.common.helpers.repeatWithLifecycle
import com.example.moviesandseries.common.helpers.show
import com.example.moviesandseries.common.models.TvShow
import com.example.moviesandseries.databinding.DetailsFragmentLayoutBinding
import com.example.moviesandseries.di.components.FragmentSubComponent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class DetailsFragment : BottomSheetDialogFragment() {

    private lateinit var fragmentSubComponent: FragmentSubComponent

    private val viewModel: MainViewModel by activityViewModels()
    private var binding: DetailsFragmentLayoutBinding? = null
    private val args: DetailsFragmentArgs by navArgs()
    private var behavior: BottomSheetBehavior<View>? = null
    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_HIDDEN, BottomSheetBehavior.STATE_COLLAPSED -> dialog?.dismiss()
                else -> {}
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSubComponent = (activity as MainActivity).activityDiComponent.fragmentComponent().create(this)
        fragmentSubComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DetailsFragmentLayoutBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repeatWithLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.belongsToFavourites.collect { result ->
                    args.tvShow.isFavourite = result
                    setupViews(args.tvShow)
                }
            }
        }

        viewModel.searchIfShowIsFavourite(args.tvShow.id)
        setupViews(args.tvShow)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        behavior?.removeBottomSheetCallback(bottomSheetCallback)
        behavior = null
        sharedElementEnterTransition = null
        binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
            override fun onStart() {
                super.onStart()
                initBottomSheetView(this)
            }
        }
    }

    private fun initBottomSheetView(dialog: BottomSheetDialog) {
        val bottomSheet: View? = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(android.R.color.transparent)
        if (bottomSheet != null) {
            initBehavior(bottomSheet)
        }
    }

    private fun initBehavior(bottomSheet: View) {
        behavior = BottomSheetBehavior.from(bottomSheet).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            addBottomSheetCallback(bottomSheetCallback)
        }
    }

    private fun setupViews(tvShow: TvShow) {
        binding?.run {
            title.text = tvShow.title
            ratingBar.rating = tvShow.voteAverage / 2
            releaseDateTv.text = tvShow.releaseDate
            addToFavBtn.isSelected = tvShow.isFavourite
            setupOverView(tvShow.overView)
            loadImage(tvShow)
            setupType(tvShow.mediaType)
            addToFavBtn.setOnClickListener {
                addOrRemoveFromList(tvShow)
            }
        }
    }

    private fun addOrRemoveFromList(tvShow: TvShow) {
        viewModel.addOrRemoveFromFavourites(tvShow)
        tvShow.isFavourite = !tvShow.isFavourite
        setupViews(tvShow)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun DetailsFragmentLayoutBinding.loadImage(tvShow: TvShow) {
        poster.scaleType = if (tvShow.posterImage.isEmpty()) {
            ImageView.ScaleType.FIT_CENTER
        } else {
            ImageView.ScaleType.CENTER_CROP
        }
        Glide.with(requireContext()).load(
            if (tvShow.posterImage.isEmpty()) ResourcesCompat.getDrawable(resources, R.drawable.ic_no_photo, null)
            else Constants.BASE_IMAGE_URL + tvShow.posterImage
        ).into(poster)
    }

    private fun DetailsFragmentLayoutBinding.setupOverView(overViewText: String) {
        with(overView) {
            if (overViewText.isEmpty()) {
                hide()
                return
            }
            text = overViewText
            show()
        }
    }

    private fun DetailsFragmentLayoutBinding.setupType(type: ShowType) {
        if (type == ShowType.UNDEFINED) {
            typeContainer.hide()
            return
        }

        if (type == ShowType.SERIES) {
            typeText.setTextColor(ResourcesCompat.getColor(resources, R.color.marsDark, null))
            typeText.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.white, null))
        }
        typeText.text = resources.getString(type.typeText)
        typeContainer.show()
    }
}