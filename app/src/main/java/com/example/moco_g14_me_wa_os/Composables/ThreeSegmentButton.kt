package com.example.moco_g14_me_wa_os.Composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable @Preview
fun ThreeSegmentButton(option1: Int, option2: Int, option3: Int){
    SingleChoiceSegmentedButtonRow{
        var selectedIndex by remember { mutableStateOf(0) }
        val options = mutableListOf<String>("$option1", "$option2", "$option3")
        options.forEachIndexed { index, option ->
            SegmentedButton(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                )
            )
            {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = option
                )
            }
        }
    }
}
