package com.dotrothschild.mysamplesensorswithlivedata.model.data

import com.dotrothschild.mysamplesensorswithlivedata.model.Rank
import kotlinx.coroutines.flow.flowOf

object OtherRepository {
    val others = flowOf(
        listOf(
            Rank(0,"Recruit", "rif_0",null, ""),
            Rank(1,"rifle ", "rif_1",null, ""),
            Rank(2,"rifle ", "rif_2",null, ""),
            Rank(3,"rifle ", "para_0",null, ""),
            Rank(4,"d", "para_1",null, ""),
            Rank(5,"e", "para_2",null, ""),
            Rank(6,"f", "para_3",null, ""),
            Rank(9,"i", "para_4",null, "")
        )
    )
}