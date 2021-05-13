package com.winas_lesson.android.day6.Day6Sample.helper

import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.winas_lesson.android.day6.Day6Sample.data.model.ContactUser
import com.winas_lesson.android.day6.Day6Sample.ui.AbstractActivity
import com.winas_lesson.android.day6.Day6Sample.ui.getContactsWithPermissionCheck

interface ContactManagerListener {
    fun onFetchUsers(users: List<ContactUser>)
}

class ContactManager {
    companion object {
        val shared = ContactManager()
    }
    var listener: ContactManagerListener? = null
    private var contacts: ArrayList<ContactUser>? = null

    fun start(from: AbstractActivity) {
        from.getContactsWithPermissionCheck()
    }
    fun fetch(from: AbstractActivity) {
        // https://medium.com/swlh/the-good-the-bad-and-the-ugly-three-approaches-to-loading-contacts-in-your-android-application-c96eaf03ffaf
        if (contacts == null) {
            LoaderManager.getInstance(from).initLoader(0, null, object : LoaderManager.LoaderCallbacks<Cursor> {
                override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                    return CursorLoader(
                        from,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        arrayOf(
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        ),
                        null,
                        null,
                        null
                    )
                }
                override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
                    if (data != null && contacts == null) {
                        var users: ArrayList<ContactUser> = arrayListOf()
                        while (!data.isClosed && data.moveToNext()) {
                            //val contactId = data.getLong(0)
                            val userName = data.getString(1)
                            val phone = data.getString(2)
                            val user = ContactUser()
                            user.userName = userName
                            user.phoneNumber = phone
                            users.add(user)
                        }
                        data.close()
                        contacts = users
                        listener?.onFetchUsers(users)
                    }
                }
                override fun onLoaderReset(loader: Loader<Cursor>) {
                    // do nothing
                }
            })
        } else {
            listener?.onFetchUsers(contacts ?: listOf())
        }
    }
}
