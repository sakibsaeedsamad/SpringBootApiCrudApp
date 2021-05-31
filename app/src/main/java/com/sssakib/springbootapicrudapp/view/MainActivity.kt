package com.sssakib.springbootapicrudapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.sssakib.springbootapicrudapp.R
import com.sssakib.springbootapicrudapp.adapter.ListOfUserAdapter
import com.sssakib.springbootapicrudapp.model.User
import com.sssakib.springbootapicrudapp.retrofit.RetrofitClient
import com.sssakib.springbootapicrudapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_dialog.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), ListOfUserAdapter.OnRowClickListener {

    lateinit var listOfUserAdapter: ListOfUserAdapter
    lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)





        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        viewCustomerButton.setOnClickListener {

            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                listOfUserAdapter = ListOfUserAdapter(this@MainActivity)
                adapter = listOfUserAdapter
            }


            viewModel.getAllUsersObservers().observe(this, Observer {
                listOfUserAdapter.setListData(ArrayList(it))
                listOfUserAdapter.notifyDataSetChanged()
            })


        }


    }

    override fun onUpdateClick(user: User) {
        val uId = user.id
        val uName = user.name
        val uAge = user.age
        val uPhone = user.number
        val uGender = user.gender
        val uLocation = user.location
        val uImage = user.image


        val intent = Intent(this@MainActivity, UpdateActivity::class.java)
        intent.putExtra("id", uId)
        intent.putExtra("name", uName)
        intent.putExtra("age", uAge)
        intent.putExtra("phone", uPhone)
        intent.putExtra("gender", uGender)
        intent.putExtra("location", uLocation)
        intent.putExtra("image", uImage)
        startActivity(intent)

    }

    override fun onDeleteClick(user: User) {

        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .customView(R.layout.activity_dialog)

        dialog.userDeleteOkButton.setOnClickListener {

            viewModel.deleteUserInfo(user)
            dialog.dismiss()
        }
        dialog.show()
    }


}