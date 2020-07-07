package com.splitoil.travel.flowcontrol.domain

import com.splitoil.travel.flowcontrol.application.command.CreateFlowControlCommand

class FlowControlCreationTest extends FlowControlTest {

    def 'Should create new flow control'() {
        given: 'Create flow command'
            def createFlowControlCommand = new CreateFlowControlCommand(TRAVEL_ID, CAR_ID, TRAVEL_ID)

        when:
            def newFlowControl = flowControlFacade.createFlowControl(createFlowControlCommand)

        then:
            with(newFlowControl) {
                travelId == TRAVEL_ID
                carId == CAR_ID
                travelId != null
            }
    }

    def 'Should get flow control details'() {
        setup:
            def flowControlId = withFlowControlOfCarAndTravel(CAR_ID, TRAVEL_ID)

        when:
            def flowControl = flowControlFacade.getFlowControl(flowControlId)

        then:
            with(flowControl) {
                travelId == TRAVEL_ID
                carId == CAR_ID
                travelId != flowControlId
            }
    }
}
