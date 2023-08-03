package me.varoa.studentprofiles.feature.favorite.screen

import android.graphics.Typeface
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import logcat.logcat
import me.varoa.studentprofiles.R
import me.varoa.studentprofiles.core.domain.model.SquadType
import me.varoa.studentprofiles.core.domain.model.StudentMinified
import me.varoa.studentprofiles.core.domain.model.TacticRole
import me.varoa.studentprofiles.core.util.ImageUtil
import me.varoa.studentprofiles.feature.favorite.databinding.ItemFavoriteBinding
import me.varoa.studentprofiles.feature.favorite.screen.FavoriteAdapter.FavoriteItemViewHolder
import me.varoa.studentprofiles.utils.STUDENT_COMPARATOR
import me.varoa.studentprofiles.viewbinding.viewBinding

class FavoriteAdapter(
    private val imageLoader: ImageLoader,
    private val onClick: (StudentMinified) -> Unit,
) : PagingDataAdapter<StudentMinified, FavoriteItemViewHolder>(STUDENT_COMPARATOR) {
    inner class FavoriteItemViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StudentMinified?) {
            logcat { "bind($data)" }
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
                // squad type
                chipSquadType.setChipBackgroundColorResource(
                    when (data.squadType) {
                        SquadType.Striker.key -> R.color.brand_type_striker
                        else -> R.color.brand_type_special
                    },
                )
                chipSquadType.text = data.squadType.uppercase()
                chipSquadType.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
                // role
                chipRole.chipIcon =
                    ContextCompat.getDrawable(
                        context,
                        when (data.tacticRole) {
                            TacticRole.Dealer.key -> R.drawable.role_damagedealer
                            TacticRole.Tank.key -> R.drawable.role_tanker
                            TacticRole.Healer.key -> R.drawable.role_healer
                            TacticRole.Supporter.key -> R.drawable.role_supporter
                            else -> R.drawable.role_vehicle
                        },
                    )
                chipRole.text = data.tacticRole
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FavoriteItemViewHolder = FavoriteItemViewHolder(parent.viewBinding(ItemFavoriteBinding::inflate))

    override fun onBindViewHolder(
        holder: FavoriteItemViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))
}
