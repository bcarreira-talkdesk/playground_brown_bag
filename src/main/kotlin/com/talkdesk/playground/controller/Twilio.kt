package com.talkdesk.playground.controller

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Call
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.*
import com.twilio.type.PhoneNumber
import com.twilio.type.Client
import com.twilio.type.Twiml
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * Change Programmable Voice -> SIP Domains
 *
 * From:
 * https://central-web.meza.talkdeskqa.com/twilio/call
 * https://central-web.meza.talkdeskqa.com/twilio/fallback
 * https://central-web.meza.talkdeskqa.com/twilio/end_call
 *
 * To:
 * https://2f69421cc362.ngrok.io/twilio/call_dial
 * https://2f69421cc362.ngrok.io/twilio/call_reject
 * https://2f69421cc362.ngrok.io/twilio/call_end
 */
@RestController
class Twilio {

    init{
        Twilio.init("xxxx", "xxxx")
    }

    @PostMapping("/twilio/call_reject", produces = [MediaType.APPLICATION_XML_VALUE])
    @ResponseBody
    fun rejectCall(): String {
        val reject = Reject.Builder().reason(Reject.Reason.REJECTED).build()
        return VoiceResponse.Builder().reject(reject).build().toXml()
    }

    @PostMapping("/twilio/call_busy", produces = [MediaType.APPLICATION_XML_VALUE])
    @ResponseBody
    fun busyCall(): String {
        val reject = Reject.Builder().reason(Reject.Reason.BUSY).build()
        return VoiceResponse.Builder().reject(reject).build().toXml()
    }

    @PostMapping("/twilio/call_dial", produces = [MediaType.APPLICATION_XML_VALUE])
    @ResponseBody
    fun handleCallToAgent(
            @RequestParam("Caller") callerId: String,
            @RequestParam("Caller") CalledId: String,
            @RequestParam("SipHeader_X-interaction_id") interactionId: String,
            @RequestParam("SipHeader_X-route_to_agent_id") agentId: String): String {


        val conference = Conference.Builder(interactionId)
                .startConferenceOnEnter(true)
                .endConferenceOnExit(true)
                .muted(false)
                .trim(Conference.Trim.TRIM_SILENCE)
                .build()

        Call.creator(
                Client(agentId),
                PhoneNumber(CalledId.replace(Regex("sip:(\\d+)@.*"), "+$1")),
                Twiml(VoiceResponse.Builder()
                        .play(Play.Builder().url("http://talkdeskapp.s3.amazonaws.com/assets/talkdesk_agent_joined_conference_signal.mp3").build())
                        .dial(Dial.Builder().conference(conference).build())
                        .hangup(Hangup.Builder().build())
                    .build().toXml())
        ).create()

        return VoiceResponse.Builder()
                .dial(Dial.Builder()
                    .answerOnBridge(true)
                    .conference(conference)
                    .build())
                .build().toXml()
    }

    @PostMapping("/twilio/call_end", produces = [MediaType.APPLICATION_XML_VALUE])
    @ResponseBody
    fun callEnd(): String {
        return VoiceResponse.Builder().build().toXml()
    }
}