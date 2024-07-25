package com.project.samay.presentation.domains

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.serialization.Serializable


@Serializable
data class NavAddDomainScreen(val isUpdate: Boolean = false)

@Composable
fun AddDomainScreen(viewModel: DomainViewModel, isUpdate: Boolean, navController: NavController) {
    val domain = viewModel.uiStateValue.value.selectedDomain
    val allDomains by viewModel.allDomains.collectAsState(initial = emptyList())
    val context = LocalContext.current
//    val isUpdate = domain != null
    var name by remember { mutableStateOf(domain?.name ?: "") }
    var description by remember { mutableStateOf(domain?.description?: "") }
    var monthlyTarget by remember { mutableStateOf(domain?.monthlyTarget ?: "") }
    var expectedPercentage by remember { mutableStateOf<String>(domain?.expectedPercentage?.toString()
        ?: "") }
    Scaffold(
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Text(
                    text = if(isUpdate) "Update your domain" else "Insert new domain",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(36.dp))
                Text("Enter the name of the domain:")
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name of domain") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(36.dp))

                Text("What parameters should be fulfilled by a task of this domain:")
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(36.dp))

                Text("Enter your vision you want to achieve:")
                OutlinedTextField(
                    value = monthlyTarget,
                    onValueChange = { monthlyTarget = it },
                    label = { Text("Monthly Target") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(36.dp))

                Text("Enter the expected percentage:")
                OutlinedTextField(
                    value = expectedPercentage,
                    onValueChange = { expectedPercentage = it },
                    label = { Text("Expected Percentage") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(36.dp))
                Text(text = "Current total percent is: ${viewModel.getTotalExpectedPercentSum(allDomains, expectedPercentage.toFloatOrNull())}")
                Spacer(modifier = Modifier.height(36.dp))

                Button(
                    onClick = {
                        if(viewModel.getTotalExpectedPercentSum(allDomains, expectedPercentage.toFloatOrNull()) > 100){
                            viewModel.showToast(context, "Total percentage cannot be more than 100")
                            return@Button
                        }
                        // Handle save action
                        if (!isUpdate) {
                            if (viewModel.saveNewDomain(
                                    name = name,
                                    description = description,
                                    monthlyTarget = monthlyTarget,
                                    expectedPercent = expectedPercentage,
                                    context = context
                                )
                            ) {
                                navController.navigateUp()
                            }
                        } else {
                                if (viewModel.updateDomain(
                                        name = name,
                                        description = description,
                                        monthlyTarget = monthlyTarget,
                                        expectedPercent = expectedPercentage,
                                        context = context
                                    )
                                ) {
                                    navController.navigateUp()
                                }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Save")
                }
            }
        }
    }
}