package com.splitoil

import com.splitoil.infrastructure.json.JacksonAdapter
import org.springframework.hateoas.EntityModel

class TestUtils {

    static Object extractContent(String resultJson) {
        JacksonAdapter.getInstance().jsonDecode(resultJson, EntityModel).getContent()
    }
}
