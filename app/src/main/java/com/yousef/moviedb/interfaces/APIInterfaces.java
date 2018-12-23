package com.yousef.moviedb.interfaces;

        import com.yousef.moviedb.dataclasses.MoviesListingResponse;

        import retrofit2.Call;
        import retrofit2.http.GET;
        import retrofit2.http.Query;

public class APIInterfaces {
    static String API_KEY = "e5c9f968d7973d7718a85beff93f389f";

    public interface ListMoviesApi {
        @GET("discover/movie/")
        Call<MoviesListingResponse> loadMovies(@Query("api_key") String apiKey,@Query("page") int page, @Query("sort_by") String sortBy);
    }


}
