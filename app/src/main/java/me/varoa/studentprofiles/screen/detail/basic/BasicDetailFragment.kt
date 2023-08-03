package me.varoa.studentprofiles.screen.detail.basic

import android.annotation.SuppressLint
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.request.ImageRequest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.base.BaseFragment
import me.varoa.studentprofiles.core.domain.model.AttackType
import me.varoa.studentprofiles.core.domain.model.DefenseType
import me.varoa.studentprofiles.core.domain.model.SquadType
import me.varoa.studentprofiles.core.domain.model.Student
import me.varoa.studentprofiles.core.domain.model.TacticRole
import me.varoa.studentprofiles.core.util.ImageUtil
import me.varoa.studentprofiles.databinding.FragmentBasicDetailBinding
import me.varoa.studentprofiles.screen.detail.DetailViewModel
import me.varoa.studentprofiles.utils.BlurTransformation
import me.varoa.studentprofiles.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BasicDetailFragment : BaseFragment(R.layout.fragment_basic_detail) {
    override val binding by viewBinding<FragmentBasicDetailBinding>()
    override val viewModel by viewModel<DetailViewModel>()

    override fun bindView() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.student.collectLatest(::loadStudent)
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun loadStudent(data: Student) {
        logcat { data.toString() }
        with(binding) {
            tvName.text = data.name
            ivBackground.apply {
                val imgData =
                    ImageRequest.Builder(requireContext())
                        .data(ImageUtil.generateBackgroundImageUrl(data.profile.bgImgPath))
                        .target(this)
                        .transformations(
                            listOf(
                                BlurTransformation(
                                    scale = 1f,
                                    radius = 5,
                                ),
                            ),
                        )
                        .allowHardware(true).build()
                imageLoader.enqueue(imgData)
            }
            ivPortrait.apply {
                val imgData =
                    ImageRequest.Builder(requireContext())
                        .data(ImageUtil.generatePortraitImageUrl(data.profile.devName))
                        .target(this)
                        .allowHardware(true).build()
                imageLoader.enqueue(imgData)
            }
            // squad type
            chipSquadType.setChipBackgroundColorResource(
                when (data.squadType) {
                    SquadType.Striker -> R.color.brand_type_striker
                    else -> R.color.brand_type_special
                },
            )
            chipSquadType.text = data.squadType.name.uppercase()
            chipSquadType.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
            // role
            chipRole.chipIcon =
                ContextCompat.getDrawable(
                    requireContext(),
                    when (data.tacticRole) {
                        TacticRole.Dealer -> R.drawable.role_damagedealer
                        TacticRole.Tank -> R.drawable.role_tanker
                        TacticRole.Healer -> R.drawable.role_healer
                        TacticRole.Supporter -> R.drawable.role_supporter
                        else -> R.drawable.role_vehicle
                    },
                )
            chipRole.text = data.tacticRole.name
            // atk type
            chipAtkType.setChipBackgroundColorResource(
                when (data.attackType) {
                    AttackType.Explosive -> R.color.brand_atk_explosive
                    AttackType.Piercing -> R.color.brand_atk_piercing
                    else -> R.color.brand_atk_mystic
                },
            )
            chipAtkType.text = data.attackType.name
            // def type
            chipDefType.setChipBackgroundColorResource(
                when (data.defenseType) {
                    DefenseType.Light -> R.color.brand_atk_explosive
                    DefenseType.Heavy -> R.color.brand_atk_piercing
                    else -> R.color.brand_atk_mystic
                },
            )
            chipDefType.text = data.defenseType.name
            // position
            chipPosition.text = data.position.name.uppercase()
            chipPosition.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
            // school
            ivSchool.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    resources.getIdentifier("school_${data.school.key.lowercase()}", "drawable", requireContext().packageName),
                ),
            )
        }
    }
}
