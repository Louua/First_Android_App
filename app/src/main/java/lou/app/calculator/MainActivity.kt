package lou.app.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lou.app.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var display by remember { mutableStateOf("0") }
    var operand1 by remember { mutableStateOf(0.0) }
    var operand2 by remember { mutableStateOf(0.0) }
    var operation by remember { mutableStateOf("") }
    var waitingForOperand by remember { mutableStateOf(false) }

    // Helper function for calculations
    fun calculate(first: Double, second: Double, op: String): Double {
        return when (op) {
            "+" -> first + second
            "-" -> first - second
            "×" -> first * second
            "÷" -> if (second != 0.0) first / second else 0.0
            else -> second
        }
    }

    fun inputNumber(number: String) {
        if (waitingForOperand) {
            display = number
            waitingForOperand = false
        } else {
            display = if (display == "0") number else display + number
        }
    }

    fun inputOperation(nextOperation: String) {
        val inputValue = display.toDoubleOrNull() ?: 0.0

        if (operand1 == 0.0) {
            operand1 = inputValue
        } else if (operation.isNotEmpty()) {
            operand2 = inputValue
            val result = calculate(operand1, operand2, operation)
            display = if (result % 1.0 == 0.0) {
                result.toInt().toString()
            } else {
                result.toString()
            }
            operand1 = result
        }

        waitingForOperand = true
        operation = nextOperation
    }

    fun performCalculation() {
        val inputValue = display.toDoubleOrNull() ?: 0.0

        if (operand1 != 0.0 && operation.isNotEmpty()) {
            operand2 = inputValue
            val result = calculate(operand1, operand2, operation)
            display = if (result % 1.0 == 0.0) {
                result.toInt().toString()
            } else {
                result.toString()
            }
            operand1 = 0.0
            operand2 = 0.0
            operation = ""
            waitingForOperand = true
        }
    }

    fun clear() {
        display = "0"
        operand1 = 0.0
        operand2 = 0.0
        operation = ""
        waitingForOperand = false
    }

    fun inputDecimal() {
        if (waitingForOperand) {
            display = "0."
            waitingForOperand = false
        } else if (!display.contains(".")) {
            display += "."
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF1E1E1E)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Display
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = display,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.White,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }

            // Button Grid
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Row 1: AC, +/-, %, ÷
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "AC",
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFF505050),
                        onClick = { clear() }
                    )
                    CalculatorButton(
                        text = "+/-",
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFF505050),
                        onClick = {
                            if (display != "0") {
                                display = if (display.startsWith("-")) {
                                    display.substring(1)
                                } else {
                                    "-$display"
                                }
                            }
                        }
                    )
                    CalculatorButton(
                        text = "%",
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFF505050),
                        onClick = {
                            val value = display.toDoubleOrNull() ?: 0.0
                            display = (value / 100).toString()
                        }
                    )
                    CalculatorButton(
                        text = "÷",
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFFFF9500),
                        onClick = { inputOperation("÷") }
                    )
                }

                // Row 2: 7, 8, 9, ×
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton("7", Modifier.weight(1f)) { inputNumber("7") }
                    CalculatorButton("8", Modifier.weight(1f)) { inputNumber("8") }
                    CalculatorButton("9", Modifier.weight(1f)) { inputNumber("9") }
                    CalculatorButton(
                        text = "×",
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFFFF9500),
                        onClick = { inputOperation("×") }
                    )
                }

                // Row 3: 4, 5, 6, -
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton("4", Modifier.weight(1f)) { inputNumber("4") }
                    CalculatorButton("5", Modifier.weight(1f)) { inputNumber("5") }
                    CalculatorButton("6", Modifier.weight(1f)) { inputNumber("6") }
                    CalculatorButton(
                        text = "-",
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFFFF9500),
                        onClick = { inputOperation("-") }
                    )
                }

                // Row 4: 1, 2, 3, +
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton("1", Modifier.weight(1f)) { inputNumber("1") }
                    CalculatorButton("2", Modifier.weight(1f)) { inputNumber("2") }
                    CalculatorButton("3", Modifier.weight(1f)) { inputNumber("3") }
                    CalculatorButton(
                        text = "+",
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFFFF9500),
                        onClick = { inputOperation("+") }
                    )
                }

                // Row 5: 0, ., =
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "0",
                        modifier = Modifier.weight(2f),
                        onClick = { inputNumber("0") }
                    )
                    CalculatorButton(
                        text = ".",
                        modifier = Modifier.weight(1f),
                        onClick = { inputDecimal() }
                    )
                    CalculatorButton(
                        text = "=",
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFFFF9500),
                        onClick = { performCalculation() }
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF333333),
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(80.dp)
            .clip(if (text == "0") RoundedCornerShape(40.dp) else CircleShape),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    CalculatorTheme {
        CalculatorApp()
    }
}