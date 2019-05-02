package che.codes.posts.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import che.codes.posts.R
import che.codes.posts.data.models.Comment
import che.codes.posts.data.models.Post
import che.codes.posts.ui.util.AvatarLoader
import com.mikhaellopez.hfrecyclerviewkotlin.HFRecyclerView
import kotlinx.android.synthetic.main.activity_post_list.view.*
import kotlinx.android.synthetic.main.header_post_details.view.*
import kotlinx.android.synthetic.main.row_post.view.author_image
import kotlinx.android.synthetic.main.row_post.view.body
import kotlinx.android.synthetic.main.row_post.view.title

class CommentsAdapter(private val baseAvatarUrl: String) : HFRecyclerView<Comment>(true, true) {

    var post: Post? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var status: String = ""
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        class ItemViewHolder(private val view: View, private val baseAvatarUrl: String) : ViewHolder(view) {

            fun bind(comment: Comment) {
                view.body.text = comment.body

                AvatarLoader.load(comment.email, view.author_image, baseAvatarUrl)
            }
        }

        class HeaderViewHolder(private val view: View, private val baseAvatarUrl: String) :
            RecyclerView.ViewHolder(view) {

            fun bind(post: Post?) {
                view.title.text = post?.title
                view.body.text = post?.body
                view.author_username.text = post?.user?.username?.toUpperCase()

                AvatarLoader.load(post?.user?.email, view.author_image, baseAvatarUrl)
            }
        }

        class FooterViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

            fun bind(status: String) {
                view.status_text.text = status
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.ItemViewHolder -> holder.bind(getItem(position))
            is ViewHolder.HeaderViewHolder -> holder.bind(post)
            is ViewHolder.FooterViewHolder -> holder.bind(status)
        }
    }

    override fun getHeaderView(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder? {
        return ViewHolder.HeaderViewHolder(inflater.inflate(R.layout.header_post_details, parent, false), baseAvatarUrl)
    }

    override fun getFooterView(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder? {
        return ViewHolder.FooterViewHolder(inflater.inflate(R.layout.footer_post_details, parent, false))
    }

    override fun getItemView(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder.ItemViewHolder(inflater.inflate(R.layout.row_comment, parent, false), baseAvatarUrl)
    }
}