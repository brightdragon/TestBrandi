package com.bd.testbrandi.data.repository

import com.bd.testbrandi.data.network.ApiService
import com.bd.testbrandi.model.SearchResponse

class SearchRepository(private val api: ApiService) {

    suspend fun getSearchImages(query: String, sort: String, page: Int, size: Int): SearchResponse {
        return api.searchImage(
            query = query,
            sort = sort,
            page = page,
            size = size
        )
    }
}