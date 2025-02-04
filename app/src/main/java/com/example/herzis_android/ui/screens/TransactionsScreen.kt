package com.example.herzis_android.ui.screens

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.herzis_android.data.local.Transaction
import com.example.herzis_android.ui.TransactionViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.*

@Composable
fun TransactionsScreen(transactionViewModel: TransactionViewModel) {
    val transactions by transactionViewModel.transactions.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var lastMonth = ""

            items(transactions) { transaction ->
                val currentMonth =
                    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(transaction.date)
                if (currentMonth != lastMonth) {
                    lastMonth = currentMonth
                    MonthHeader(month = currentMonth)
                }

                TransactionItem(transaction = transaction)
            }
        }
    }
}

@Composable
fun MonthHeader(month: String) {
    Text(
        text = month,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        color = Color.DarkGray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TimelineMarker(isPositive = transaction.positiv)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (transaction.positiv) Color.Black else Color.Red
                )
            }

            Text(
                text = formatDate(transaction.date),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 8.dp),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}

@Composable
fun TimelineMarker(isPositive: Boolean) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .background(
                color = if (isPositive) Color.Green else Color.Red,
                shape = androidx.compose.foundation.shape.CircleShape
            )
    )
}

fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return dateFormat.format(date)
}
