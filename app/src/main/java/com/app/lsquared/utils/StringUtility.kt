package com.app.lsquared.utils

class StringUtility {

    companion object{

        fun getFormattedString(desc: String): String {
            var description = ""
            if(!desc.contains("<p>")) return desc
            else if(desc.contains("<p>")){
                var desc_p = desc.split("<p>")
                var new_desc =  desc_p[1].split("</p>")
                return new_desc[0]
            }
            return description
        }


    }


}