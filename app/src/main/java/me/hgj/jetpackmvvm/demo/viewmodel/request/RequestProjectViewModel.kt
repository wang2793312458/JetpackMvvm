package me.hgj.jetpackmvvm.demo.viewmodel.request

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState
import me.hgj.jetpackmvvm.demo.data.model.bean.AriticleResponse
import me.hgj.jetpackmvvm.demo.data.model.bean.ClassifyResponse
import me.hgj.jetpackmvvm.demo.data.repository.request.HttpRequestManger
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/28
 * 描述　:
 */
class RequestProjectViewModel(application: Application) : BaseViewModel(application){

    //页码
    var pageNo = 1

    var titleData: MutableLiveData<ResultState<ArrayList<ClassifyResponse>>> = MutableLiveData()

    var projectDataState: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()
    
    fun getProjectTitleData() {
        request({ HttpRequestManger.apiService.getProjecTitle() }, titleData)
    }

    fun getProjectData(isRefresh: Boolean, cid: Int, isNew: Boolean = false) {
        if (isRefresh) {
            pageNo = if (isNew) 0 else 1
        }
        request({ HttpRequestManger.instance.getProjectData(pageNo, cid, isNew)},{
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            projectDataState.postValue(listDataUiState)
        },{
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
            projectDataState.postValue(listDataUiState)
        })
    }
}