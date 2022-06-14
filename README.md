# Pixabay Image Search

Hi! This is an example app which tries to fulfill all the requirements of the task.

## Requirements
From the list of requirements the following are omitted for brevity:

- Security - Ideally one shouldn't hard code the API KEY in the build.gradle but rather keep it parametrized in a local file which is not checked into the VCS history. The local file should be read at runtime and the API KEY then injected into the BuildConfig. This makes sure that all users use their own API KEY
- Offline Caching - the PixabayApiClient would not be the class that is used by the ViewModel in a production version of the app, rather it would be something like a PixabayImageRepository which in turn would have both a PixabayApiClient and a PixabayCache. Depending on the requirements and the invalidation strategy one would first load results from the cache and then hit the network if necessary.

## Architecture

App is structured using an MVVM pattern.
Kotlin coroutines are used to achieve concurrency and kotlin Flows are used to introduce reactivity.
The ViewModel exposes **both** UI State and Emphemeral State (one-off events such as showing prompts and giving navigation directions), called _state_ and _actions_ respectively. These are exposed in StateFlows for the ViewModel consumer to observe.


## Testing

In order to keep things brief, the SearchViewModel is unit testing, to give the readers an example of how unit tests are usually to be structured. 