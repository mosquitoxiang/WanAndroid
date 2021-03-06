package com.illu.demo.ui.mine.collection

import androidx.lifecycle.MutableLiveData
import com.illu.demo.base.BaseViewModel
import com.illu.demo.bean.ArticleBean
import com.illu.demo.common.UserManager
import com.illu.demo.common.bus.Bus
import com.illu.demo.common.bus.USER_COLLECT_UPDATE
import com.illu.demo.common.loadmore.LoadMoreStatus

class CollectionViewModel : BaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    private var page = INITIAL_PAGE

    val refreshStatus = MutableLiveData<Boolean>()
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val emptyDataStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()
    val articleList = MutableLiveData<MutableList<ArticleBean>>()

    fun refreshData() {
        refreshStatus.value = true
        emptyDataStatus.value = false
        reloadStatus.value = false
        launch(
            block = {
                val article = mRespository.getCollectionList(INITIAL_PAGE)
                article.datas.forEach { it.collect = true }
                page = article.curPage
                articleList.value = article.datas.toMutableList()
                emptyDataStatus.value = article.datas.isEmpty()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = true
            }
        )
    }

    fun loadMore() {
        loadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val article = mRespository.getCollectionList(page + 1)
                val newList = articleList.value ?: mutableListOf()
                newList.addAll(article.datas)
                articleList.value = newList
                loadMoreStatus.value = if (article.offset >= article.total) {
                    LoadMoreStatus.END
                } else {
                    LoadMoreStatus.COMPLETED
                }
            },
            error = {
                loadMoreStatus.value = LoadMoreStatus.ERROR
            }
        )
    }

    fun unCollect(id: Int) {
        launch(
            block = {
                mRespository.unCollect(id)
                UserManager.removeCollectId(id)
                Bus.post(USER_COLLECT_UPDATE, id to false)
            }
        )
    }
}