package che.codes.posts.ui.activities

import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import che.codes.posts.PostsApplication
import che.codes.posts.R
import che.codes.posts.data.models.Post
import che.codes.posts.ui.adapters.CommentsAdapter
import che.codes.posts.viewmodels.PostDetailsViewModel
import che.codes.posts.viewmodels.PostDetailsViewModel.FetchResult
import che.codes.posts.viewmodels.PostsViewModelFactory
import javax.inject.Inject

const val POST_KEY = "POST_KEY"

class PostDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: PostsViewModelFactory

    @Inject
    lateinit var commentsAdapter: CommentsAdapter

    private lateinit var commentList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        setContentView(R.layout.activity_post_details)

        PostsApplication.instance.component.inject(this)

        val post: Post? = intent.extras[POST_KEY] as Post?

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(PostDetailsViewModel::class.java)
        viewModel.result.observe(this, Observer<FetchResult> { result -> processResult(result) })
        viewModel.post = post

        commentsAdapter.post = post

        commentList = findViewById<RecyclerView>(R.id.comment_list).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@PostDetailsActivity)
            adapter = commentsAdapter
        }

        viewModel.fetchDetails()
    }

    private fun processResult(result: FetchResult) {
        when (result) {
            is FetchResult.Fetching -> {
                commentsAdapter.status = getString(R.string.fetching)
            }
            is FetchResult.Error -> {
                commentsAdapter.status = getString(R.string.error)
            }
            is FetchResult.Success -> {
                if (result.comments.isEmpty()) {
                    commentsAdapter.status = getString(R.string.no_comments)
                } else {
                    commentsAdapter.status = ""
                }

                commentsAdapter.data = result.comments
            }
        }
    }
}