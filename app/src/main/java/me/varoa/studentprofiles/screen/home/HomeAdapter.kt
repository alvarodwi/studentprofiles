package me.varoa.studentprofiles.screen.home

import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.core.domain.model.SquadType
import me.varoa.studentprofiles.core.domain.model.StudentMinified
import me.varoa.studentprofiles.core.domain.model.TacticRole
import me.varoa.studentprofiles.core.util.ImageUtil
import me.varoa.studentprofiles.databinding.ItemStudentBinding
import me.varoa.studentprofiles.screen.home.HomeAdapter.StudentItemViewHolder
import me.varoa.studentprofiles.utils.STUDENT_COMPARATOR
import me.varoa.studentprofiles.viewbinding.viewBinding

class HomeAdapter(
    private val imageLoader: ImageLoader,
    private val onClick: (StudentMinified) -> Unit,
) : PagingDataAdapter<StudentMinified, StudentItemViewHolder>(STUDENT_COMPARATOR) {
    inner class StudentItemViewHolder(private val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StudentMinified?) {
            if (data == null) return
            val context = binding.root.context
            with(binding) {
                root.setOnClickListener { onClick(data) }
                tvName.text = data.name
                ivPhoto.apply {
                    val imgData =
                        ImageRequest.Builder(context)
                            .data(ImageUtil.generateCollectionImageUrl(data.imgPath))
                            .target(this)
                            .allowHardware(true).build()
                    imageLoader.enqueue(imgData)
                }
                cardSquadType.setCardBackgroundColor(
                    context.getColor(
                        when (data.squadType) {
                            SquadType.Striker.key -> R.color.brand_type_striker
                            else -> R.color.brand_type_special
                        },
                    ),
                )
                ivRole.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        when (data.tacticRole) {
                            TacticRole.Dealer.key -> R.drawable.role_damagedealer
                            TacticRole.Healer.key -> R.drawable.role_healer
                            TacticRole.Supporter.key -> R.drawable.role_supporter
                            TacticRole.Tank.key -> R.drawable.role_tanker
                            else -> R.drawable.role_vehicle
                        },
                        null,
                    ),
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StudentItemViewHolder = StudentItemViewHolder(parent.viewBinding(ItemStudentBinding::inflate))

    override fun onBindViewHolder(
        holder: StudentItemViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))
}
