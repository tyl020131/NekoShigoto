package com.example.testemail

import android.app.Activity
import android.app.ProgressDialog
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.example.nekoshigoto.MainActivity
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

private lateinit var progressDialog : ProgressDialog
private const val SMTP_HOST_NAME = "smtp.gmail.com"
private const val SMTP_PORT = "587"
private const val EMAIL_FROM = "khttg3@gmail.com"
private const val EMAIL_PASSWORD = "puiixgmzqysykhhc"

class SendEmail(private val activity: Activity) : AsyncTask<String, Void, Boolean>() {

    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Sending email...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    override fun doInBackground(vararg params: String?): Boolean {
        try {
            val emailTo = params[0]
            val body = params[1]
            val subject = params[2]

            val props: Properties = Properties()
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.starttls.enable"] = "true"
            props["mail.smtp.host"] = SMTP_HOST_NAME
            props["mail.smtp.port"] = SMTP_PORT

            val session: Session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication? {
                    return PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD)
                }
            })

            val message: Message = MimeMessage(session)
            message.setFrom(InternetAddress(EMAIL_FROM))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo))
            message.subject = subject
            message.setText(body)

            Transport.send(message)

            return true
        } catch (e: MessagingException) {
            Log.e("fail", e.stackTraceToString())
            return false
        }
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        progressDialog.dismiss()
        if (result) {
            Toast.makeText(activity, "Successfully sent an email", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "Failed to send email", Toast.LENGTH_SHORT).show()
        }
    }
}