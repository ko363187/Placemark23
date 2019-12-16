package org.wit.placemark.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor
import org.wit.placemark.R
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.UserModel

import com.google.firebase.auth.FirebaseUser

import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.R.attr.password




class LoginActivity : AppCompatActivity(),View.OnClickListener{

    private var isLogin = false
    lateinit var app : MainApp
    var user = UserModel()
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.wit.placemark.R.layout.activity_login)
        app = application as MainApp
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        decorateText()
        initializelistener()
    }

    override fun onStart() {
        super.onStart()



    }

    private fun initializelistener() {
        btn_login.setOnClickListener(this)
    }
    //log
    override fun onClick(p0: View?) {
       if (p0 == btn_login){
           if (!isLogin) {
               login()
           } else {
               signUp()
           }
       }
    }

    private fun login() {
         user.email = useremail.text.toString()
         user.password = userPass.text.toString()

        if (!validateEditText(useremail.text.toString())) {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show()
        }
        else if (!isValidEmail(useremail.text.toString())){
            Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show()
        }
        else if (!validateEditText(userPass.text.toString())) {
            Toast.makeText(this, "Password is not valid", Toast.LENGTH_SHORT).show()
        }
        /*
         else if (app.users.login(user)){
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
         */

        else {
            // Intent intent = new Intent(this,Video.class);
            //startActivity(intent);
            signInWithFirebase()
        }

        useremail.setText("")
        userPass.setText("")
    }

    fun validateEditText(text: String): Boolean {
        return !TextUtils.isEmpty(text) && text.trim { it <= ' ' }.length > 0
    }

    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun signUp() {
        user.email = useremail.text.toString()
        user.password = userPass.text.toString()


        if (!validateEditText(useremail.text.toString())) {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show()
        }else if(!isValidEmail(useremail.text.toString())){
            Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show()
        }
        else if (!validateEditText(userPass.text.toString())) {
            Toast.makeText(this, "Password is not valid", Toast.LENGTH_SHORT).show()
        }
        /*
         else if(app.users.signup(user)){
            Toast.makeText(this, "Sign Up failed", Toast.LENGTH_SHORT).show()
        }
         */

        else {
           // app.users.create(user.copy())
            signUpOnFirebase()
           // Toast.makeText(this, "Sign Up successful", Toast.LENGTH_SHORT).show()
        }

        useremail.setText("")
        userPass.setText("")
    }


    private fun signUpOnFirebase(){
      mAuth.createUserWithEmailAndPassword(useremail.text.toString(),userPass.text.toString())
           .addOnCompleteListener(this) { task ->
                  if (task.isSuccessful) {
                       // Sign in success, update UI with the signed-in user's information
                      Toast.makeText(this, "Sign Up successful", Toast.LENGTH_SHORT).show()
                        val user = mAuth.currentUser
                       //updateUI(user)
                   }
                  else {
                        // If sign in fails, display a message to the user.
                        // Log.w(FragmentActivity.TAG, "createUserWithEmail:failure", task.exception)
                         //Toast.makeText(this@EmailPasswordActivity, "Authentication failed.",
                        //  Toast.LENGTH_SHORT).show()
                         // updateUI(null)
                      Toast.makeText(this, "Sign Up failed", Toast.LENGTH_SHORT).show()
                  }
           }
    }

    private fun signInWithFirebase(){
        mAuth.signInWithEmailAndPassword(useremail.text.toString(),userPass.text.toString())
             .addOnCompleteListener(this) { task ->
           if (task.isSuccessful) {
                 // Sign in success, update UI with the signed-in user's information
                 // Log.d(FragmentActivity.TAG, "signInWithEmail:success")
                 val user = mAuth.currentUser
                 // updateUI(user)
               startActivity(intentFor<Dashboard>())
               overridePendingTransition(
                   org.wit.placemark.R.anim.abc_fade_in,
                   org.wit.placemark.R.anim.abc_fade_out
               )
               Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

             } else {
                 // If sign in fails, display a message to the user.
                   //Log.w(FragmentActivity.TAG, "signInWithEmail:failure", task.exception)
                 //  Toast.makeText(this@EmailPasswordActivity, "Authentication failed.",
                 //    Toast.LENGTH_SHORT).show()
                 //updateUI(null)
               Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
           }


        }
    }

    private fun setSignUp() {
        if (!isLogin) {
            btn_login.setText("Sign Up")
            isLogin = true
            decorateLoginText()
        }
    }


    private fun decorateText() {
        val ss = SpannableString(getString(org.wit.placemark.R.string.don_t_have_an_account_sign_up))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                setSignUp()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        ss.setSpan(clickableSpan, 23, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, org.wit.placemark.R.color.colorPrimary)), 23,
            ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvSignup.setText(ss)
        tvSignup.setMovementMethod(LinkMovementMethod.getInstance())
        tvSignup.setHighlightColor(Color.TRANSPARENT)
    }

    private fun decorateLoginText() {
        val ss = SpannableString(getString(org.wit.placemark.R.string.alraedy_have_account_login))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                setLogin()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        ss.setSpan(clickableSpan, 24, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)), 24,
            ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvSignup.setText(ss)
        tvSignup.setMovementMethod(LinkMovementMethod.getInstance())
        tvSignup.setHighlightColor(Color.TRANSPARENT)
    }
    private fun setLogin() {
        if (isLogin) {
            btn_login.setText("Login")
            isLogin = false
            decorateText()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


}
