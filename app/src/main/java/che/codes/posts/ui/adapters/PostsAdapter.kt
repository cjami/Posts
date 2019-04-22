package che.codes.posts.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import che.codes.posts.R
import che.codes.posts.data.models.Post
import che.codes.posts.ui.activities.POST_KEY
import che.codes.posts.ui.activities.PostDetailsActivity
import che.codes.posts.ui.util.AvatarLoader
import com.mikhaellopez.hfrecyclerviewkotlin.HFRecyclerView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.GrayscaleTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import jp.wasabeef.picasso.transformations.gpu.VignetteFilterTransformation
import kotlinx.android.synthetic.main.row_post.view.*

class PostsAdapter(private val baseAvatarUrl: String) : HFRecyclerView<Post>(true, false) {

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        class ItemViewHolder(private val view: View, private val baseAvatarUrl: String) : ViewHolder(view),
            View.OnClickListener {
            private var post: Post? = null

            init {
                view.setOnClickListener(this)
            }

            fun bind(post: Post) {
                this.post = post

                view.title.text = post.title
                view.body.text = post.body

                AvatarLoader(post.user.email, view.author_image, baseAvatarUrl).load()
            }

            override fun onClick(v: View?) {
                val context = v?.context
                val intent = Intent(context, PostDetailsActivity::class.java)
                intent.putExtra(POST_KEY, post)
                context?.startActivity(intent)
            }
        }

        class HeaderViewHolder(view: View) : ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.ItemViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getHeaderView(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder? {
        return ViewHolder.HeaderViewHolder(inflater.inflate(R.layout.header_post_list, parent, false))
    }

    override fun getFooterView(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder? {
        return null
    }

    override fun getItemView(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder.ItemViewHolder(inflater.inflate(R.layout.row_post, parent, false), baseAvatarUrl)
    }
}