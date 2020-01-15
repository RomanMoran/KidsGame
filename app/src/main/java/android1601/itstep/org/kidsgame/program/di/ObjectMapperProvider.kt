package android1601.itstep.org.kidsgame.program.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object ObjectMapperProvider : IndependentProvider<Gson>() {

    override fun initInstance(): Gson = GsonBuilder()
            .create()

}
