package com.bd.testbrandi.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bd.testbrandi.data.network.ApiService
import com.bd.testbrandi.model.Documents
import retrofit2.HttpException
import java.io.IOException

class SearchSource(val query: String): PagingSource<Int, Documents>() {

    override fun getRefreshKey(state: PagingState<Int, Documents>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Documents> {
        return try{
            val nextPage = params.key ?: 1
            val documentList = ApiService.ApiClient.apiService.searchImage(
                query = query,
                sort = "",
                page = nextPage,
                size = 30
            )

            LoadResult.Page(
                data = documentList.documents,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (documentList.documents.isEmpty()) null else nextPage + 1
            )
        }catch (exception: IOException){
            return LoadResult.Error(exception)
        }catch (exception : HttpException){
            return LoadResult.Error(exception)
        }

    }
}