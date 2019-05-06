package che.codes.posts.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import che.codes.posts.core.data.PostsDataSource
import che.codes.posts.features.details.PostDetailsViewModel
import che.codes.posts.features.list.PostListViewModel

class PostsViewModelFactory(private val dataSource: PostsDataSource) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PostListViewModel::class.java) -> {
                PostListViewModel(dataSource) as T
            }
            modelClass.isAssignableFrom(PostDetailsViewModel::class.java) -> {
                PostDetailsViewModel(dataSource) as T
            }
            else -> throw IllegalArgumentException(
                "${modelClass.simpleName} is an unknown view model type"
            )
        }
    }
}