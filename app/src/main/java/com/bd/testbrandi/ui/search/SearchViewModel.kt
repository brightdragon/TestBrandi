package com.bd.testbrandi.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bd.testbrandi.data.repository.SearchSource
import com.bd.testbrandi.model.Documents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce

class SearchViewModel: ViewModel() {

    private val _documents = MutableLiveData<Documents>()
    val documents: LiveData<Documents> = _documents

    fun settingDocument(ducument: Documents){
        _documents.value = ducument
    }

    fun search(query: String): Flow<PagingData<Documents>> {
        return Pager(PagingConfig(pageSize = 20)) {
            SearchSource(query)
        }.flow.cachedIn(viewModelScope).debounce(1000L)
    }
}