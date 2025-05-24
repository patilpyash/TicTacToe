package com.yash.tictactoe45562

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.tictactoe45562.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TicTacToeGame()
                }
            }
        }
    }
}

enum class Player { X, O }

@Composable
fun TicTacToeGame() {
    var board by remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf(Player.X) }
    var winner by remember { mutableStateOf<String?>(null) }
    var moveHistory by remember { mutableStateOf(mutableListOf<Pair<Int, Int>>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tic Tac Toe", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(24.dp))

        for (i in 0..2) {
            Row {
                for (j in 0..2) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(1.dp, Color.Black)
                            .background(Color.White)
                            .clickable(enabled = board[i][j].isEmpty() && winner == null) {
                                if (board[i][j].isEmpty()) {
                                    val newBoard = board.map { it.toMutableList() }.toMutableList()
                                    val newHistory = moveHistory.toMutableList()
                                    newBoard[i][j] = currentPlayer.name
                                    newHistory.add(i to j)

                                    val result = checkWinner(newBoard)

                                    if (result == "Draw") {
                                        // Remove last 3 moves
                                        repeat(3) {
                                            if (newHistory.isNotEmpty()) {
                                                val (x, y) = newHistory.removeLast()
                                                newBoard[x][y] = ""
                                            }
                                        }
                                        board = newBoard
                                        moveHistory = newHistory
                                    } else {
                                        board = newBoard
                                        moveHistory = newHistory
                                        winner = if (result != null) result else null
                                        if (result == null) {
                                            currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
                                        }
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(board[i][j], fontSize = 36.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        winner?.let {
            Text("Winner: $it", fontSize = 28.sp, color = Color.Green)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                board = List(3) { MutableList(3) { "" } }
                winner = null
                currentPlayer = Player.X
                moveHistory.clear()
            }) {
                Text("Restart")
            }
        }
    }
}

fun checkWinner(board: List<List<String>>): String? {
    for (i in 0..2) {
        if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != "")
            return board[i][0]
        if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != "")
            return board[0][i]
    }

    if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != "")
        return board[0][0]
    if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != "")
        return board[0][2]

    if (board.flatten().none { it == "" }) return "Draw"

    return null
}
