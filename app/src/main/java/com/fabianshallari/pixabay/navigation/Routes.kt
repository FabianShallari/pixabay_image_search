package com.fabianshallari.pixabay.navigation

object Routes {
    const val Search = "search"
    const val Confirmation = "confirmation"

    object ImageDetail {
        private const val RootRoute = "image_detail"
        const val RouteTemplate = "$RootRoute/{${Arguments.PixabayImage}}"
        fun routeWithId(id: Long): String {
            return "$RootRoute/$id"
        }
    }
}