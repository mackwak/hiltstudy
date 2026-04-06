package com.example.uppmanageapp1.shop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.graphics.Color

@Composable
fun ShopifyProductsScreen(viewModel: ShopViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState().value

    when (uiState) {
        is ShopUiState.Loading -> {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                CircularProgressIndicator()
            }
        }
        is ShopUiState.Error -> {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(text = "오류: ${uiState.message}", style = MaterialTheme.typography.bodyLarge)
            }
        }
        is ShopUiState.Success -> {
            ProductList(products = uiState.products, onItemClick = { /* TODO: 상세 화면으로 이동 */ })
        }
    }
}

@Composable
fun ProductList(products: List<Product>, onItemClick: (Product) -> Unit) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(products) { product ->
            ProductItem(product = product, onClick = { onItemClick(product) })
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // 간단한 레이아웃: 이미지 대신 박스 자리 표시자로 유지
            val context = LocalContext.current
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Column(modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
            ) {
                Text(text = product.title, style = MaterialTheme.typography.titleMedium)
                Text(text = product.price, style = MaterialTheme.typography.bodyMedium)
            }

            var isFavorite = remember { mutableStateOf(false) }

            IconButton(onClick = { isFavorite.value = !isFavorite.value }) {
                Icon(
                    imageVector = if (isFavorite.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite.value) "즐겨찾기 해제" else "즐겨찾기",
                    tint = if (isFavorite.value) Color.Red else Color.Gray
                )
            }
        }
    }
}
