package com.app.lsquared.network;

import com.app.lsquared.model.vimeo.VimeoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VimeoInterface {
    @GET("{video_id}/config")
    Call<VimeoResponse> getVimeoUrlResponse(@Path("video_id") String id);

}
