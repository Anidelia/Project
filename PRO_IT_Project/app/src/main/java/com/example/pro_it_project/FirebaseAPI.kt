package com.example.pro_it_project

class FirebaseAPI {

    fun writeUserFB(name: String, mail: String, surname: String, completion: (Int) -> Unit) {
        FirebaseDatabase.getInstance().getReference("Users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val count = dataSnapshot.children.count() + 1
                    FirebaseDatabase.getInstance().getReference("Users").child(count.toString())
                        .child("Mail").setValue(mail)
                    FirebaseDatabase.getInstance().getReference("Users").child(count.toString())
                        .child("Name").setValue(name)
                    FirebaseDatabase.getInstance().getReference("Users").child(count.toString())
                        .child("Surname").setValue(surname)
                    val filenameA = "ava" + (1..5).random() + ".jpg"
                    val filenameF = "fon" + (1..5).random() + ".jpg"
                    FirebaseDatabase.getInstance().getReference("Users").child(count.toString())
                        .child("Photo").child("Avatarka").setValue(filenameA)
                    FirebaseDatabase.getInstance().getReference("Users").child(count.toString())
                        .child("Photo").child("Fon").setValue(filenameF)
                    completion(count)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //Обработка ошибки
                    println("Failed to read value: ${databaseError.toException()}")
                }
            })
    }
}