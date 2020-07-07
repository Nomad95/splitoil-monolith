package com.splitoil.travel.flowcontrol

import com.splitoil.IntegrationTest
import com.splitoil.base.IntegrationSpec
import com.splitoil.infrastructure.security.WithMockSecurityContext
import com.splitoil.travel.flowcontrol.web.dto.AddGpsPointCommand
import com.splitoil.travel.flowcontrol.web.dto.AddWaypointReachedGpsPointCommand
import com.splitoil.travel.flowcontrol.web.dto.GeoPointDto
import org.junit.experimental.categories.Category
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Category(IntegrationTest)
@WithMockUser("admin")
@WithMockSecurityContext
class FlowControlIntegrationTest extends IntegrationSpec {

    static final UUID FLOW_CONTROL_ID = UUID.fromString("f5fe3daa-7077-4cf8-9b96-ea62cd041d16")
    static final UUID WAYPOINT_ID = UUID.fromString('29c9c416-cad6-4845-97a0-3d2b1bf496e9')

    @Sql(scripts = ["/db/travel/flowcontrol/new_flow_control.sql"])
    def "System receives gps signal while travelling"() {
        given:
            def command = new AddGpsPointCommand(flowControlId: FLOW_CONTROL_ID, geoPoint: GeoPointDto.of(1, 1))

        when:
            def result = mockMvc.perform(post("/flowcontrol/receivegpspoint")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(command)))

        then: 'Point added'
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())

        when: 'Get points view'
            result = mockMvc.perform(get("/flowcontrol/query/gps/" + FLOW_CONTROL_ID)
                    .contentType(MediaType.APPLICATION_JSON))

        then: 'Point added'
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.gpsPoints[0].geoPoint.lon').value(1))
                    .andExpect(jsonPath('$.gpsPoints[0].geoPoint.lat').value(1))
    }

    @Sql(scripts = ["/db/travel/flowcontrol/new_flow_control.sql"])
    def "System receives gps signal with waypoint while travelling"() {
        given:
            def command = new AddWaypointReachedGpsPointCommand(
                    flowControlId: FLOW_CONTROL_ID,
                    geoPoint: GeoPointDto.of(1, 1),
                    waypointId: WAYPOINT_ID,
                    waypointType: 'CHECKPOINT')

        when:
            def result = mockMvc.perform(post("/flowcontrol/receivewaypoint")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(command)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())

        when: 'Get points view'
            result = mockMvc.perform(get("/flowcontrol/query/gps/" + FLOW_CONTROL_ID)
                    .contentType(MediaType.APPLICATION_JSON))

        then: 'Point added'
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.gpsPoints[0].associatedWaypointId').value(WAYPOINT_ID.toString()))
                    .andExpect(jsonPath('$.gpsPoints[0].waypointType').value('CHECKPOINT'))
                    .andExpect(jsonPath('$.gpsPoints[0].geoPoint.lon').value(1))
                    .andExpect(jsonPath('$.gpsPoints[0].geoPoint.lat').value(1))
    }
}
