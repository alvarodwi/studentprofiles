package me.varoa.studentprofiles.screen.detail.profile

import android.graphics.Typeface
import androidx.lifecycle.lifecycleScope
import coil.request.ImageRequest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.base.BaseFragment
import me.varoa.studentprofiles.core.domain.model.Student
import me.varoa.studentprofiles.core.util.ImageUtil
import me.varoa.studentprofiles.databinding.FragmentProfileDetailBinding
import me.varoa.studentprofiles.ext.decodeHtml
import me.varoa.studentprofiles.screen.detail.DetailViewModel
import me.varoa.studentprofiles.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileDetailFragment : BaseFragment(R.layout.fragment_profile_detail) {
    override val binding by viewBinding<FragmentProfileDetailBinding>()
    override val viewModel by viewModel<DetailViewModel>()

    override fun bindView() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.student.collectLatest(::loadStudent)
        }
    }

    private fun loadStudent(data: Student) {
        with(binding) {
            ivPhoto.apply {
                val imgData =
                    ImageRequest.Builder(requireContext())
                        .data(ImageUtil.generateCollectionImageUrl(data.imgPath))
                        .target(this)
                        .allowHardware(true).build()
                imageLoader.enqueue(imgData)
            }
            // profile
            tvFullName.text = data.profile.fullName
            tvSchool.text = data.school.fullName
            tvSchoolYear.text = data.profile.schoolYear
            tvClub.text = data.profile.club
            // other info
            tvCharacterVoice.text = data.profile.cv
            tvBirthday.text = data.profile.birthday
            tvAge.text = data.profile.age
            tvHeight.text = data.profile.height
            tvDesign.text = data.profile.designer
            tvIllustrator.text = data.profile.illustrator
            tvHobbies.text = data.profile.hobbies.decodeHtml()
            // basic info
            tvBasicInfo.text = data.profile.basicInfo.decodeHtml()
            // weapon
            tvWeaponType.text = data.weaponType.name
            tvWeaponType.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
            ivWeapon.apply {
                val imgData =
                    ImageRequest.Builder(requireContext())
                        .data(ImageUtil.generateWeaponImageUrl(data.profile.weaponImgPath))
                        .target(this)
                        .allowHardware(true).build()
                imageLoader.enqueue(imgData)
            }
        }
    }
}
