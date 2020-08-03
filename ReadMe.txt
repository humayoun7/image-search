This app is developed using Architecture Components https://developer.android.com/jetpack/guide


Libraries used:
- Retrofit for REST api communication
- Glide for image loading
- Photoview for image viewing and zooming
- Android Architecture Components
    - navigation
    - lifecycle
    - paging 3.0 
        * Requests data when the user is close to the end of the list.
        * Handles in-memory cache.
        

Note: 
- Some random images are displayed on launch, any new images will appear on search.
- The app also takes care of retry logic on network failures. 
- If the description is available for the image it is showed on tapping the image when viewing image

