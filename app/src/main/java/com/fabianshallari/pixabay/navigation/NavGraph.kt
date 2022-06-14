package com.fabianshallari.pixabay.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavType
import androidx.navigation.createGraph
import androidx.navigation.fragment.dialog
import androidx.navigation.fragment.fragment
import com.fabianshallari.pixabay.confirmation.ConfirmationFragment
import com.fabianshallari.pixabay.detail.ImageDetailFragment
import com.fabianshallari.pixabay.search.SearchFragment

fun NavController.createPixabayNavGraph(): NavGraph =
    createGraph(startDestination = Routes.Search) {
        fragment<SearchFragment>(Routes.Search)
        fragment<ImageDetailFragment>(Routes.ImageDetail.RouteTemplate) {
            argument(Arguments.PixabayImage) {
                type = NavType.LongType
                nullable = false
            }
        }
        dialog<ConfirmationFragment>(Routes.Confirmation)
    }