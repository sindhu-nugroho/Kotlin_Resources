package com.example.my_wallet.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// --- DATA MODEL ---
data class Transaction(
    val id: Int,
    val title: String,
    val amount: String,
    val date: String,
    val isIncome: Boolean,
)

// --- STATELESS COMPONENT (UI ONLY) ---
@Composable
fun BalanceDisplay(
    balance: String,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = if (isVisible) balance else "••••••••",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
        )
        IconButton(onClick = onToggleVisibility) {
            Icon(
                imageVector = if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "Toggle Balance",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(transaction.title, style = MaterialTheme.typography.titleMedium)
            Text(transaction.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Text(
            text = (if (transaction.isIncome) "+" else "-") + transaction.amount,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = if (transaction.isIncome) Color(0xFF4CAF50) else Color(0xFFE53935),
        )
    }
}

// --- SLOT-BASED LAYOUT ---
@Composable
fun WalletCard(
    title: @Composable () -> Unit,
    balance: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            title()   // Slot 1
            balance() // Slot 2

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                actions() // Slot 3
            }
        }
    }
}

// --- DASHBOARD UI (STATEFUL) ---
@Composable
fun EWalletDashboard(modifier: Modifier = Modifier) {
    var isBalanceVisible by remember { mutableStateOf(true) }
    var selectedFilter by remember { mutableStateOf("Semua") }

    val allTransactions = listOf(
        Transaction(1, "Top Up", "500.000", "12 Feb 2024", true),
        Transaction(2, "Bayar Listrik", "200.000", "11 Feb 2024", false),
        Transaction(3, "Transfer Masuk", "1.000.000", "10 Feb 2024", true),
        Transaction(4, "Beli Makan", "50.000", "09 Feb 2024", false),
    )

    val filteredTransactions = when (selectedFilter) {
        "Masuk" -> allTransactions.filter { it.isIncome }
        "Keluar" -> allTransactions.filter { !it.isIncome }
        else -> allTransactions
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Header Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Halo, Sindhu!",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Member Gold",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }

        // Wallet Card Section
        WalletCard(
            title = { Text("Total Saldo", style = MaterialTheme.typography.labelLarge) },
            balance = {
                // Menggunakan Stateless Component
                BalanceDisplay(
                    balance = "Rp 2.500.000",
                    isVisible = isBalanceVisible,
                    onToggleVisibility = { isBalanceVisible = !isBalanceVisible },
                )
            },
            actions = {
                ActionButton(icon = Icons.Default.Add, label = "Top Up")
                ActionButton(icon = Icons.AutoMirrored.Filled.Send, label = "Kirim")
                ActionButton(icon = Icons.Default.History, label = "Riwayat")
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Riwayat Section (Filter Chips)
        Text(
            "Riwayat Transaksi",
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf("Semua", "Masuk", "Keluar").forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                )
            }
        }

        // Transaction List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            items(filteredTransactions) { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}

@Composable
fun ActionButton(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { /* Aksi */ }
            .padding(8.dp),
    ) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}
