package me.varoa.studentprofiles.screen.home

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import me.varoa.studentprofiles.core.domain.model.StudentMinified
import me.varoa.studentprofiles.core.util.ImageUtil
import me.varoa.studentprofiles.databinding.ItemStudentBinding
import me.varoa.studentprofiles.screen.home.HomeAdapter.StudentItemViewHolder
import me.varoa.studentprofiles.utils.STUDENT_COMPARATOR
import me.varoa.studentprofiles.viewbinding.viewBinding

class HomeAdapter(
    private val onClick: (StudentMinified) -> Unit,
    private val imageLoader: ImageLoader,
) : ListAdapter<StudentMinified, StudentItemViewHolder>(STUDENT_COMPARATOR) {
    inner class StudentItemViewHolder(private val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StudentMinified?) {
            if (data == null) return
            val context = binding.root.context
            with(binding) {
                val imgData = ImageRequest.Builder(context)
                    .data(ImageUtil.generateCollectionImageUrl(data.devName))
                    // .target(this).transformations(RoundedCornersTransformation(16f))
                    .allowHardware(true).build()
                imageLoader.enqueue(imgData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentItemViewHolder =
        StudentItemViewHolder(parent.viewBinding(ItemStudentBinding::inflate))

    override fun onBindViewHolder(holder: StudentItemViewHolder, position: Int) =
        holder.bind(getItem(position))
}
