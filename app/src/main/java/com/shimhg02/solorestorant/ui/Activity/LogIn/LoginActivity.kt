package com.shimhg02.solorestorant.ui.Activity.LogIn


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.network.Data.LoginData.LogIn
import com.shimhg02.solorestorant.network.Retrofit.Client
import com.shimhg02.solorestorant.ui.Activity.Main.MainActivity
import com.shimhg02.solorestorant.ui.Activity.SignUp.SignUpPhoneActivity
import com.shimhg02.solorestorant.ui.Activity.Term.TermActivity
import com.shimhg02.solorestorant.utils.Bases.BaseActivity
import com.shimhg02.solorestorant.utils.Preference.SharedPref
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * @description 로그인 액티비티
 */


class LoginActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener {
    val PREFERENCE = "com.shimhg02.honbab"
    override var viewId: Int = R.layout.activity_login
    var callbackManager = CallbackManager.Factory.create();
    private lateinit var googleSignInClient: GoogleSignInClient
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate() {
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        SharedPref.openSharedPrep(this)
        facebookLogin()
        googleLogin()
        AutoLogin()
        System.out.println("token Test1 : "+ pref.getString("fbToken",""))
        System.out.println("token Test2 : "+ pref.getString("ggToken",""))
        login_btn.setOnClickListener { login() }
        signup_go.setOnClickListener {  startActivity<SignUpPhoneActivity>() }
    }


    private fun AutoLogin(){
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        if (pref.getString("token", "").toString() !== "") {
            Client.retrofitService.autoLogin(pref.getString("token","").toString()).enqueue(object :
                Callback<LogIn> {
                override fun onResponse(call: Call<LogIn>?, response: Response<LogIn>?) {
                    when (response!!.code()) {
                        200 -> {
                            startActivity<MainActivity>()
                            finish()
                        }
                        203-> {
                            alertTermDialog()
                        }
                        500 -> Toast.makeText(this@LoginActivity, "서버 점검중입니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<LogIn>?, t: Throwable?) {

                }
            })
        }
    }

    private fun login() {
        login_btn.isClickable = false
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val editor = pref.edit()
            Client.retrofitService.logIn(id_tv.text.toString(), pw_tv.text.toString()).enqueue(object : Callback<LogIn> {
            override fun onResponse(call: Call<LogIn>?, response: Response<LogIn>?) {
                when (response!!.code()) {
                    200 -> {
                        editor.putString("userName", response.body()!!.name.toString())
                        editor.putString("token", response.body()!!.token.toString())
                        editor.putString("uuid", response.body()!!.uuid.toString())
                        editor.putString("nick", response.body()!!.nick.toString())
                        editor.apply()
                        startActivity<MainActivity>()
                        finish()
                    }
                    203 -> {
                        editor.putString("userName", response.body()!!.name.toString())
                        editor.putString("token", response.body()!!.token.toString())
                        editor.putString("uuid", response.body()!!.uuid.toString())
                        editor.putString("nick", response.body()!!.nick.toString())
                        editor.apply()
                        login_btn.isClickable = true
                        alertTermDialog()
                    }
                    404 -> {
                        login_btn.isClickable = true
                        Toast.makeText(
                            this@LoginActivity,
                            "로그인 실패: PW나 ID를 다시 확인하세요.",
                            Toast.LENGTH_LONG).show()
                    }
                    500 -> {
                        login_btn.isClickable = true
                        Toast.makeText(this@LoginActivity, "서버 점검중입니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show()
                    }
                }
            }
            override fun onFailure(call: Call<LogIn>?, t: Throwable?) {

            }
        })
    }

    private fun facebookOauthCall(){
        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(this)
    }

    private fun facebookLogin(){
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val editor = pref.edit()
        lateinit var auth: FirebaseAuth
        fb_btn.setOnClickListener {
            facebookOauthCall()
            var FacebookLoginBtn = findViewById(R.id.fb_btn) as LoginButton
            FacebookLoginBtn.setReadPermissions("email", "public_profile")
            FacebookLoginBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    var accessTokenTracker = object : AccessTokenTracker() {
                        override fun onCurrentAccessTokenChanged(
                            oldAccessToken: AccessToken,
                            currentAccessToken: AccessToken
                        ) {
                            // Set the access token using
                            // currentAccessToken when it's loaded or set.
                        }
                    }
                    var accessToken = AccessToken.getCurrentAccessToken()
                    handleFacebookAccessToken(accessToken)
                    System.out.println("TOKENS $accessToken")
                    System.out.println("TOKENS ${accessToken.token}")
                }
                override fun onCancel() {
                    Toast.makeText(this@LoginActivity,"OnCancle",Toast.LENGTH_SHORT).show()
                }
                override fun onError(exception: FacebookException) {
                    Toast.makeText(this@LoginActivity,"OnError",Toast.LENGTH_SHORT).show()
                }
            })
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")
        auth = FirebaseAuth.getInstance()
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                        user!!.getIdToken(true)
                            .addOnCompleteListener { task ->
                            if (task.isSuccessful()) {
                                val idToken: String? = task.getResult()!!.getToken()
                                System.out.println("TESTTOKEN$idToken")
                                Client.retrofitService.getFacebookAccess(idToken!!).enqueue(object : Callback<Void> {
                                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                                        when (response!!.code()) {
                                            200 -> {
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "체크 성공",
                                                Toast.LENGTH_LONG).show()
                                        }
                                            201 -> {
                                                Toast.makeText(
                                                    this@LoginActivity,
                                                    "체크 성공, 이미 있는 유저",
                                                   Toast.LENGTH_LONG).show()
                                            }
                                            401 -> {
                                                Toast.makeText(
                                                    this@LoginActivity,
                                                    "이메일 미아",
                                                    Toast.LENGTH_LONG).show()
                                            }
                                            404 -> {
                                                Toast.makeText(
                                                    this@LoginActivity,
                                                    "없는 유저",
                                                    Toast.LENGTH_LONG).show()
                                            }
                                            500 -> Toast.makeText(this@LoginActivity, "서버 점검중입니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<Void>?, t: Throwable?) {

                                    }
                                })
                            } else {
                            }
                        }
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }



    private fun googleLogin() {
        google_btn.setOnClickListener {
            getGoogleApi()
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun getGoogleApi(){
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount): Unit {
        mAuth = FirebaseAuth.getInstance();
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val editor = pref.edit()

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
//        val decodedToken = FirebaseToken = FirebaseAuth.getInstance().verifyIdToken(idToken)
        mAuth!!.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (!task.isSuccessful()) {
                Toast.makeText(this@LoginActivity, "실패", Toast.LENGTH_SHORT).show()
            } else {
                System.out.println("GGToken: "+  acct.id +","+acct.account+", Token:" +acct.idToken)
                editor.putString("ggToken",acct.idToken)
                editor.apply()
                val mUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                mUser!!.getIdToken(true)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful()) {
                            val idToken: String? = task.getResult()!!.getToken()
                            Client.retrofitService.getGoogleAccess(idToken!!).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                                    when (response!!.code()) {
                                        200 -> {
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "체크 성공",
                                                Toast.LENGTH_LONG).show()
                                        }
                                        201 -> {
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "체크 성공, 이미 있는 유저",
                                                Toast.LENGTH_LONG).show()
                                        }
                                        401 -> {
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "이메일 미아",
                                                Toast.LENGTH_LONG).show()
                                        }
                                        404 -> {
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "없는 유저",
                                                Toast.LENGTH_LONG).show()
                                        }
                                        500 -> Toast.makeText(this@LoginActivity, "서버 점검중입니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show()
                                    }
                                }

                                override fun onFailure(call: Call<Void>?, t: Throwable?) {

                                }
                            })
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                Toast.makeText(this@LoginActivity, "성공", Toast.LENGTH_SHORT).show()

            }
        }
    }



    private fun alertTermDialog(){
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val editor = pref.edit()
        var dialog = AlertDialog.Builder(this@LoginActivity)
        dialog.setTitle("약관 동의")
        dialog.setMessage("개인정보 수집 약관에 동의를 하지 않으셔서 로그인을 할 수 없습니다.\n동의 페이지로 넘어갈까요?")
        dialog.setIcon(R.mipmap.ic_launcher)

        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->{
                        startActivity<TermActivity>()
                        finish()
                    }
                    DialogInterface.BUTTON_NEGATIVE ->{
                        editor.clear()
                        Toast.makeText(this@LoginActivity, "개인정보 수집 거부 처리 되었습니다. \n동의하시고 서비스를 사용하시길 원하신다면 다시 로그인 해주세요. ", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        dialog.setPositiveButton("네",dialog_listener)
        dialog.setNegativeButton("아니오",dialog_listener)
        dialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!!.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
            } else {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

}