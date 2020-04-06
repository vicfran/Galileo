/*
 * Copyright (C) 2017 Jeff Gilfelt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.josedlpozo.galileo.chuck.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.josedlpozo.galileo.chuck.R
import com.josedlpozo.galileo.chuck.data.HttpTransaction
import com.josedlpozo.galileo.chuck.data.HttpTransactionRepository
import kotlinx.android.synthetic.main.galileo_transaction_overview_fragment.*

internal class TransactionOverviewFragment : androidx.fragment.app.Fragment() {

    companion object {
        private const val TRANSACTION_ID = "id"
        fun newInstance(id: Long): TransactionOverviewFragment =
            TransactionOverviewFragment().apply {
                val bundle = Bundle().apply {
                    putLong(TRANSACTION_ID, id)
                }

                arguments = bundle
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.galileo_transaction_overview_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val id = arguments?.getLong(TRANSACTION_ID) ?: 0
        val transaction = HttpTransactionRepository.find(id)
        transaction?.let {
            bind(it)
        }
    }

    private fun bind(transaction: HttpTransaction) {
        url.text = transaction.url
        method.text = transaction.method
        protocol.text = transaction.protocol
        status.text = transaction.status.toString()
        response.text = transaction.responseSummaryText
        ssl.text =
            if (transaction.isSsl) context?.getString(R.string.chuck_yes) else context?.getString(R.string.chuck_no)
        requestTime.text = transaction.requestDateString
        responseTime.text = transaction.responseDateString
        duration.text = transaction.durationString
        requestSize.text = transaction.requestSizeString
        responseSize.text = transaction.responseSizeString
        totalSize.text = transaction.totalSizeString
    }
}