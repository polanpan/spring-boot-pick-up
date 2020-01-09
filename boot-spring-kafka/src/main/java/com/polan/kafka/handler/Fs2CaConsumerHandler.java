package com.unioncloud.callability.kafka.handler;

import com.unioncloud.callability.common.constraint.CallAbilityConstraint;
import com.unioncloud.callability.common.proto.message.Ca2FsTopic;
import com.unioncloud.callability.common.proto.message.Fs2CaTopic;
import com.unioncloud.callability.db.mongo.dao.cdr.CalloutCdrDao;
import com.unioncloud.callability.db.mongo.dao.cdr.HoldLogDao;
import com.unioncloud.callability.db.mongo.dao.cdr.MonitorLogDao;
import com.unioncloud.callability.db.mongo.entity.FsRequestLogEntity;
import com.unioncloud.callability.db.mongo.entity.cdr.*;
import com.unioncloud.callability.db.mongo.repository.FsRequestLogRepository;
import com.unioncloud.callability.db.mongo.repository.cdr.*;
import com.unioncloud.callability.publish.CallAbilityPublishService;
import com.unioncloud.kernel.enu.CallOutResultEnum;
import com.unioncloud.kernel.enu.OperateResultEnum;
import com.unioncloud.kernel.enu.OperationStatusEnum;
import com.unioncloud.kernel.enu.audio.MixStatus;
import com.unioncloud.kernel.enu.audio.VoiceRecordSpeakerEnum;
import com.unioncloud.kernel.enu.call.CallEventTypeEnum;
import com.unioncloud.kernel.enu.call.CallOutTaskTypeEnum;
import com.unioncloud.kernel.enu.call.CallParticipantEnum;
import com.unioncloud.kernel.enu.call.CallTypeEnum;
import com.unioncloud.kernel.model.callability.publish.*;
import com.unioncloud.kernel.utils.AudioFilePathUtil;
import com.unioncloud.kernel.utils.DateUtils;
import com.unioncloud.quality.collaboration.feign.client.SyncRecordUploadFeignClient;
import com.unioncloud.quality.collaboration.feign.client.qo.QualityRecordUploadQO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>  消费数据处理</p>
 *
 * @author panliyong  2019/3/6 14:37
 */
@Slf4j
@Component
public class Fs2CaConsumerHandler {

    @Autowired
    private FsRequestLogRepository fsRequestLogRepository;

    @Autowired
    private CallAbilityPublishService callAbilityPublishService;

    @Autowired
    private MainCdrRepository mainCdrRepository;

    @Autowired
    private RobotInfoRepository robotInfoRepository;

    @Autowired
    private HoldLogRepository holdLogRepository;

    @Autowired
    private HoldLogDao holdLogDao;

    @Autowired
    private CalloutCdrRepository calloutCdrRepository;

    @Autowired
    private CalloutCdrDao calloutCdrDao;

    @Autowired
    private MonitorLogRepository monitorLogRepository;

    @Autowired
    private MonitorLogDao monitorLogDao;

    @Autowired
    private VoiceRecordInfoRepository voiceRecordInfoRepository;


    @Autowired
    private SyncRecordUploadFeignClient syncRecordUploadFeignClient;

    /**
     * <p>kafka 消息消费逻辑处理方法</p>
     *
     * @param message 消费到的消息
     * @author panliyong  2019-11-16 16:38
     */
    public void process(Fs2CaTopic.Fs2CaMessage message) {
        try {
            Fs2CaTopic.MessageNameEnum eMessageName = message.getEMessageName();
            switch (eMessageName) {
                case eCheckInResponse:
                    Fs2CaTopic.CheckInResponse stCheckInRes = message.getStCheckInRes();
                    this.processCheckInResponse(stCheckInRes);
                    return;
                case eCheckOutResponse:
                    Fs2CaTopic.CheckOutResponse stCheckOutRes = message.getStCheckOutRes();
                    this.processCheckOutResponse(stCheckOutRes);
                    return;
                case eAgentCallOutResponse:
                    Fs2CaTopic.AgentCallOutResponse stAgentCallOutRes = message.getStAgentCallOutRes();
                    this.processAgentCallOutResponse(stAgentCallOutRes);
                    return;
                case eRobotCallOutResponse:
                    Fs2CaTopic.RobotCallOutResponse stRobotCallOutRes = message.getStRobotCallOutRes();
                    this.processRobotCallOutResponse(stRobotCallOutRes);
                    return;
                case eMonitorResponse:
                    Fs2CaTopic.MonitorResponse stMonitorRes = message.getStMonitorRes();
                    this.processMonitorResponse(stMonitorRes);
                    return;
                case eUnMonitorResponse:
                    Fs2CaTopic.UnMonitorResponse stUnMonitorRes = message.getStUnMonitorRes();
                    this.processUnMonitorResponse(stUnMonitorRes);
                    return;
                case eHoldResponse:
                    Fs2CaTopic.HoldResponse stHoldRes = message.getStHoldRes();
                    this.processHoldResponse(stHoldRes);
                    return;
                case eUnHoldResponse:
                    Fs2CaTopic.UnHoldResponse stUnHoldRes = message.getStUnHoldRes();
                    this.processUnHoldResponse(stUnHoldRes);
                    return;
                case eAgentHangupResponse:
                    Fs2CaTopic.AgentHangupResponse stAgentHangupRes = message.getStAgentHangupRes();
                    this.processAgentHangupResponse(stAgentHangupRes);
                    return;
                case eAgent2AgentResponse:
                    Fs2CaTopic.Agent2AgentResponse stAgent2AgentRes = message.getStAgent2AgentRes();
                    this.processAgent2AgentResponse(stAgent2AgentRes);
                    return;
                case eAgent2RobotResponse:
                    Fs2CaTopic.Agent2RobotResponse stAgent2RobotRes = message.getStAgent2RobotRes();
                    this.processAgent2RobotResponse(stAgent2RobotRes);
                    return;
                case eRobot2AgentResponse:
                    Fs2CaTopic.Robot2AgentResponse stRobot2AgentRes = message.getStRobot2AgentRes();
                    this.processRobot2AgentResponse(stRobot2AgentRes);
                    return;
                case eAgentRing:
                    Fs2CaTopic.AgentRing stAgentRing = message.getStAgentRing();
                    this.processAgentRing(stAgentRing);
                    return;
                case eAgentAnswer:
                    Fs2CaTopic.AgentAnswer stAgentAnswer = message.getStAgentAnswer();
                    this.processAgentAnswer(stAgentAnswer);
                    return;
                case eAgentHangup:
                    Fs2CaTopic.AgentHangup stAgentHangup = message.getStAgentHangup();
                    this.processAgentHangup(stAgentHangup);
                    return;
                case eCustomerRing:
                    Fs2CaTopic.CustomerRing stCustomerRing = message.getStCustomerRingResRes();
                    this.processCustomerRing(stCustomerRing);
                    return;
                case eCustomerAnswer:
                    Fs2CaTopic.CustomerAnswer stCustomerAnswer = message.getStCustomerAnswerRes();
                    this.processCustomerAnswer(stCustomerAnswer);
                    return;
                case eCustomerHangup:
                    Fs2CaTopic.CustomerHangup stCustomerHangup = message.getStCustomerHangupRes();
                    this.processCustomerHangup(stCustomerHangup);
                    return;
                case eRobotHangup:
                    Fs2CaTopic.RobotHangup stRobotHangup = message.getStRobotHangup();
                    this.processRobotHangup(stRobotHangup);
                    return;
                case eRobot2OutResponse:
                    Fs2CaTopic.Robot2OutResponse stRobot2OutResponse = message.getStRobot2OutResponse();
                    this.processRobot2OutResponse(stRobot2OutResponse);
                    return;
                case eMonitorHangup:
                    Fs2CaTopic.MonitorHangup stMonitorHangup = message.getStMonitorHangup();
                    this.processMonitorHangup(stMonitorHangup);
                    return;
                default:
                    log.error("【Fs2CaConsumerHandler】-> Fs2CaConsumerHandler 收到的消息名称枚举无法解析");
            }
        } catch (Exception e) {
            log.error("【Fs2CaConsumerHandler】-> Fs2CaConsumerHandler 消费消息产生异常", e);
        }
    }

    /**
     * <p>监听挂断事件逻辑处理</p>
     *
     * @param request 监听挂断事件
     * @author panliyong  2019-11-28 14:22
     */
    private void processMonitorHangup(Fs2CaTopic.MonitorHangup request) {
        long triggerTime = request.getTriggerTime();
        //修改监听记录
        String monitorLogId = request.getCallerCallId();
        MonitorLogEntity monitorLogEntity = monitorLogRepository.findOne(monitorLogId);
        monitorLogEntity.setEndTime(triggerTime);
        long connectTime;
        connectTime = Objects.isNull(monitorLogEntity.getBeginTime()) ? 0L : monitorLogEntity.getBeginTime();
        monitorLogEntity.setDuration(triggerTime - connectTime);
        monitorLogRepository.save(monitorLogEntity);
    }


    /**
     * <p>机器人挂断事件推送逻辑处理</p>
     *
     * @param request 机器人挂断事件推送
     * @author panliyong  2019-11-19 17:54
     */
    private void processRobotHangup(Fs2CaTopic.RobotHangup request) {
        Fs2CaTopic.RobotHangupReason hangupReason = request.getHangupReason();
        String calledCallId = request.getCalledCallId();
        long triggerTime = request.getTriggerTime();
        CalloutCdrEntity calloutCdrEntity = calloutCdrDao.findOfNoEndRecord(calledCallId, CallParticipantEnum.ROBOT);
        if (Objects.isNull(calloutCdrEntity)) {
            return;
        }
        if (calloutCdrEntity.getHangUpTime() > 0) {
            return;
        }
        calloutCdrEntity.setHangUpTime(triggerTime);
        long connectTime = calloutCdrEntity.getConnectTime();
        if (connectTime == 0) {
            calloutCdrEntity.setCallDuration(0L);
        } else {
            calloutCdrEntity.setCallDuration(triggerTime - connectTime);
        }
        switch (hangupReason) {
            //机器人主动挂断
            case robot:
                calloutCdrEntity.setHangingReason(CallParticipantEnum.ROBOT);
                break;
            //机器人被转接导致挂断
            case transfer2Agent:
                calloutCdrEntity.setHangingReason(CallParticipantEnum.AGENT);
                break;
            //目前表示 客户挂断后 机器人挂断
            case other:
                calloutCdrEntity.setHangingReason(CallParticipantEnum.CUSTOMER);
                break;
            default:
                //如果客户没有接通就不会有这个事件
        }
        calloutCdrRepository.save(calloutCdrEntity);
        //机器人挂断暂时不传给前端
    }

    /**
     * <p>客户挂断推送事件逻辑处理</p>
     *
     * @param request 客户挂断推送事件
     * @author panliyong  2019-11-20 13:36
     */
    private void processCustomerHangup(Fs2CaTopic.CustomerHangup request) {
        String calledCallId = request.getCallId();
        Fs2CaTopic.FailReason failReason = request.getFailReason();
        long triggerTime = request.getTriggerTime();
        CallParticipantEnum hangingReasonEnum = CallParticipantEnum.CUSTOMER;
        MainCdrEntity mainCdrEntity = mainCdrRepository.findOne(calledCallId);
        if (Objects.isNull(mainCdrEntity)) {
            return;
        }
        Long connectTime = mainCdrEntity.getConnectTime();
        mainCdrEntity.setHangUpTime(triggerTime);
        CallOutResultEnum callOutResultEnum = CallOutResultEnum.connected;

        Fs2CaTopic.CallType callType = request.getCallType();
        CallEventTypeEnum callEventTypeEnum = CallEventTypeEnum.CALL_OUT;
        switch (callType) {
            case transfer:
                callEventTypeEnum = CallEventTypeEnum.TRANSFER;
                break;
            case callin:
                callEventTypeEnum = CallEventTypeEnum.CALL_IN;
                break;
            case monitor:
                callEventTypeEnum = CallEventTypeEnum.MONITOR;
                break;
            case callout:
                callEventTypeEnum = CallEventTypeEnum.CALL_OUT;
                break;
            default:
        }
        switch (callType) {
            case transfer:
                break;
            case callout: {
                CalloutCdrEntity calloutCdrEntity;

                if ((failReason == Fs2CaTopic.FailReason.none || Objects.isNull(failReason)) && connectTime != 0L) {
                    //客户接听 并通话结束
                    mainCdrEntity.setCallDuration(connectTime == 0 ? 0 : triggerTime - connectTime);
                    List<CalloutCdrEntity> calloutCdrEntities = calloutCdrRepository.findByMainCdr(mainCdrEntity);
                    calloutCdrEntities.sort(Comparator.comparing(CalloutCdrEntity::getCallTime).reversed());
                    calloutCdrEntity = calloutCdrEntities.get(0);
                    Long hangUpTime = calloutCdrEntity.getHangUpTime();
                    if (Objects.isNull(hangUpTime) || hangUpTime == 0) {
                        //说明是客户挂断事件先到
                        CallParticipantEnum callerRole = calloutCdrEntity.getCallerRole();
                        switch (callerRole) {
                            case ROBOT:
                                hangingReasonEnum = CallParticipantEnum.ROBOT;
                                break;
                            case AGENT:
                                hangingReasonEnum = CallParticipantEnum.AGENT;
                                break;
                            default:
                        }
                    }
                    mainCdrEntity.setHangingReason(hangingReasonEnum);
                    calloutCdrEntity.setHangingReason(hangingReasonEnum);
                } else {
                    //呼叫客户失败
                    mainCdrEntity.setCallDuration(0L);
                    switch (failReason) {
                        case rejected:
                            callOutResultEnum = CallOutResultEnum.rejected;
                            break;
                        case calling:
                            callOutResultEnum = CallOutResultEnum.calling;
                            break;
                        case noAnswer:
                            callOutResultEnum = CallOutResultEnum.noAnswer;
                            break;
                        case shutDown:
                            callOutResultEnum = CallOutResultEnum.shutDown;
                            break;
                        case concurrentFailed:
                            callOutResultEnum = CallOutResultEnum.concurrentFailed;
                            break;
                        case notCallOut:
                            callOutResultEnum = CallOutResultEnum.notCallout;
                            break;
                        default:
                            callOutResultEnum = CallOutResultEnum.networkBusy;
                    }
                    mainCdrEntity.setCallResult(callOutResultEnum);
                    mainCdrEntity.setHangingReason(CallParticipantEnum.CUSTOMER);
                    Long ringTime = mainCdrEntity.getRingTime();
                    mainCdrEntity.setRingDuration(ringTime == 0 ? 0 : triggerTime - ringTime);
                    mainCdrEntity = mainCdrRepository.save(mainCdrEntity);
                    List<CalloutCdrEntity> calloutCdrEntities = calloutCdrRepository.findByMainCdr(mainCdrEntity);
                    calloutCdrEntity = calloutCdrEntities.get(0);
                    calloutCdrEntity.setCallResult(callOutResultEnum);
                }
                mainCdrEntity = mainCdrRepository.save(mainCdrEntity);
                calloutCdrRepository.save(calloutCdrEntity);
            }
            break;
            case callin:
                break;
            default:
        }

        Long callDuration = mainCdrEntity.getCallDuration();
        if (callDuration > CallAbilityConstraint.CALL_RECORD_IGNORE_SECOND) {

            String path = AudioFilePathUtil.speakerAudioFileUrlCreator(DateUtils.millisecond2LocalDateTime(mainCdrEntity.getStartingTime()), calledCallId,
                    mainCdrEntity.getCaller(), mainCdrEntity.getCalled(), VoiceRecordSpeakerEnum.MIX_RESULT
            );
            log.info("【Fs2CaConsumerHandler】-> processCustomerHangup 生成的录音path={}", path);
            VoiceRecordInfoEntity voiceRecordInfoEntity = VoiceRecordInfoEntity.builder()
                    .mixStatus(MixStatus.mixed)
                    .mainCdrId(calledCallId)
                    .voiceRecordUrl(path)
                    .build();
            voiceRecordInfoEntity = voiceRecordInfoRepository.save(voiceRecordInfoEntity);
            mainCdrEntity.setVoiceRecordInfo(voiceRecordInfoEntity);
            mainCdrEntity = mainCdrRepository.save(mainCdrEntity);

            // 推送质检
            List<CalloutCdrEntity> calloutCdrEntities = calloutCdrRepository.findByMainCdr(mainCdrEntity);
            if (!CollectionUtils.isEmpty(calloutCdrEntities)) {

                try {
                    CalloutCdrEntity calloutCdrEntity = calloutCdrEntities.get(0);
                    CallOutTaskTypeEnum callOutTaskType = calloutCdrEntity.getCallOutTaskType();
                    //自主外呼不质检
                    if (!CallOutTaskTypeEnum.MANUAL_CALL.equals(callOutTaskType)) {

                        Map<String, Object> taskInfo = calloutCdrEntity.getTaskInfo();
                        Object taskId = taskInfo.get("taskId");
                        Object agentGroupId = taskInfo.get("agentGroupId");

                        Long startingTime = mainCdrEntity.getStartingTime();
                        String serviceDay = DateUtils.localDateTimeFormat(DateUtils.millisecond2LocalDateTime(startingTime), "yyyy-MM-dd");

                        QualityRecordUploadQO qo = QualityRecordUploadQO.builder()
                                .callId(calledCallId)
                                .businessTenantId(mainCdrEntity.getTenantId())
                                .platformId(1)
                                .callProjectId(calloutCdrEntity.getProjectId())
                                .callTaskId(Objects.nonNull(taskId) ? Integer.parseInt(taskId.toString()) : 0)
                                .callDetailSerial(calledCallId)
                                .serialNumber(System.currentTimeMillis() + "")
                                .callType("callout")
                                .callTypeDefine("")
                                .clientServerId(0)
                                .clientJobNumber(calloutCdrEntity.getAccount())
                                .serverNumber(mainCdrEntity.getCaller())
                                .seatGroupId(Integer.parseInt(agentGroupId.toString()))
                                .customerId(0)
                                .customerSerial("0")
                                .customerNumber(mainCdrEntity.getCalled())
                                .serviceDay(serviceDay)
                                .startTime(DateUtils.localDateTimeDefaultFormat(
                                        DateUtils.millisecond2LocalDateTime(mainCdrEntity.getConnectTime())))
                                .endTime(DateUtils.localDateTimeDefaultFormat(
                                        DateUtils.millisecond2LocalDateTime(mainCdrEntity.getHangUpTime())))
                                .longTime(mainCdrEntity.getCallDuration() / 1000)
                                .voiceMixedPath(voiceRecordInfoEntity.getVoiceRecordUrl())
                                .agentAudioUrl("")
                                .customAudioUrl("")
                                .build();
                        syncRecordUploadFeignClient.syncUploadRecord(qo);
                        log.info("【Fs2CaConsumerHandler】-> processCustomerHangup 讲话单推送到质检 {}", qo);
                    }
                } catch (Exception e) {
                    log.error("推送质检话单出现异常", e);
                }
            }
        }
        //发布 redis 客户挂断事件
        CustomerHangupEventRequest model = CustomerHangupEventRequest.builder()
                .customerPhone(mainCdrEntity.getCalled())
                .hangupRole(hangingReasonEnum)
                .calledCallId(calledCallId)
                .triggerTime(triggerTime)
                .callResult(callOutResultEnum)
                .callEventTypeEnum(callEventTypeEnum)
                .build();
        callAbilityPublishService.customerHangup(model);
    }

    /**
     * <p>客户接通事件推送处理</p>
     *
     * @param request 客户接听事件
     * @author panliyong  2019-11-19 16:31
     */
    private void processCustomerAnswer(Fs2CaTopic.CustomerAnswer request) {
        String calledCallId = request.getCallId();
        long triggerTime = request.getTriggerTime();
        Fs2CaTopic.CallType callType = request.getCallType();
        CallEventTypeEnum callEventTypeEnum = CallEventTypeEnum.CALL_OUT;
        switch (callType) {
            case transfer:
                callEventTypeEnum = CallEventTypeEnum.TRANSFER;
                break;
            case callin:
                callEventTypeEnum = CallEventTypeEnum.CALL_IN;
                break;
            case monitor:
                callEventTypeEnum = CallEventTypeEnum.MONITOR;
                break;
            case callout:
                callEventTypeEnum = CallEventTypeEnum.CALL_OUT;
                break;
            default:
        }
        MainCdrEntity mainCdrEntity = mainCdrRepository.findOne(calledCallId);
        mainCdrEntity.setConnectTime(triggerTime);
        Long ringTime = mainCdrEntity.getRingTime();
        if (Objects.isNull(ringTime) || ringTime == 0) {
            ringTime = triggerTime;
            mainCdrEntity.setRingTime(ringTime);
        }
        mainCdrEntity.setRingDuration(triggerTime - ringTime);

        mainCdrEntity.setCallResult(CallOutResultEnum.connected);
        mainCdrEntity = mainCdrRepository.save(mainCdrEntity);

        if (Fs2CaTopic.CallType.callout.equals(callType)) {
            //找到一个对应的外呼记录如果是机器人的话需要需改机器人的接通信息
            CallParticipantEnum originalCallType = mainCdrEntity.getOriginalCallType();
            if (CallParticipantEnum.ROBOT.equals(originalCallType)) {
                CalloutCdrEntity robotCalloutCdrEntity = calloutCdrDao.findOfNoEndRecord(calledCallId, CallParticipantEnum.ROBOT);
                if (Objects.nonNull(robotCalloutCdrEntity)) {
                    robotCalloutCdrEntity.setRingTime(triggerTime);
                    robotCalloutCdrEntity.setRingDuration(0L);
                    robotCalloutCdrEntity.setConnectTime(triggerTime);
                    robotCalloutCdrEntity.setCallResult(CallOutResultEnum.connected);
                    calloutCdrRepository.save(robotCalloutCdrEntity);
                }
            }
        }

        //发布 redis 客户接听事件
        CustomerAnswerEventRequest model = CustomerAnswerEventRequest.builder()
                .customerPhone(mainCdrEntity.getCalled())
                .calledCallId(calledCallId)
                .triggerTime(triggerTime)
                .callEventTypeEnum(callEventTypeEnum)
                .build();
        callAbilityPublishService.customerAnswer(model);
    }

    /**
     * <p>客户接听事件推送</p>
     *
     * @param request 客户接听事件
     * @author panliyong  2019-11-19 16:20
     */
    private void processCustomerRing(Fs2CaTopic.CustomerRing request) {
        String callId = request.getCallId();
        long triggerTime = request.getTriggerTime();
        Fs2CaTopic.CallType callType = request.getCallType();
        CallEventTypeEnum callEventTypeEnum = CallEventTypeEnum.CALL_OUT;
        switch (callType) {
            case transfer:
                callEventTypeEnum = CallEventTypeEnum.TRANSFER;
                break;
            case callin:
                callEventTypeEnum = CallEventTypeEnum.CALL_IN;
                break;
            case monitor:
                callEventTypeEnum = CallEventTypeEnum.MONITOR;
                break;
            case callout:
                callEventTypeEnum = CallEventTypeEnum.CALL_OUT;
                break;
            default:
        }

        MainCdrEntity mainCdrEntity = mainCdrRepository.findOne(callId);
        mainCdrEntity.setRingTime(triggerTime);
        mainCdrRepository.save(mainCdrEntity);
        //发布 redis 客户响铃事件
        CustomerRingEventRequest model = CustomerRingEventRequest.builder()
                .customerPhone(mainCdrEntity.getCalled())
                .calledCallId(callId)
                .triggerTime(triggerTime)
                .province(mainCdrEntity.getProvince())
                .city(mainCdrEntity.getCity())
                .operator(mainCdrEntity.getOperator())
                .callEventTypeEnum(callEventTypeEnum)
                .build();
        callAbilityPublishService.customerRing(model);
    }

    /**
     * <p>坐席挂断推送逻辑处理</p>
     *
     * @param request 坐席挂断事件
     * @author panliyong  2019-11-19 17:43
     */
    private void processAgentHangup(Fs2CaTopic.AgentHangup request) {
        String callerCallId = request.getCallId();
        long triggerTime = request.getTriggerTime();

        Fs2CaTopic.CallType callType = request.getCallType();
        CallEventTypeEnum callEventTypeEnum = CallEventTypeEnum.CALL_OUT;
        switch (callType) {
            case transfer:
                callEventTypeEnum = CallEventTypeEnum.TRANSFER;
                break;
            case callin:
                callEventTypeEnum = CallEventTypeEnum.CALL_IN;
                break;
            case monitor:
                callEventTypeEnum = CallEventTypeEnum.MONITOR;
                break;
            case callout:
                callEventTypeEnum = CallEventTypeEnum.CALL_OUT;
                break;
            default:
        }
        MainCdrEntity mainCdr = null;
        String agentAccount = "";
        switch (callType) {
            case transfer:
            case callout:
                CalloutCdrEntity calloutCdrEntity = calloutCdrRepository.findOne(callerCallId);
                if (calloutCdrEntity.getHangUpTime() > 0) {
                    break;
                }
                calloutCdrEntity.setHangUpTime(triggerTime);
                long connectTime = calloutCdrEntity.getConnectTime();
                if (connectTime == 0) {
                    calloutCdrEntity.setCallDuration(0L);
                } else {
                    calloutCdrEntity.setCallDuration(triggerTime - connectTime);
                }
                mainCdr = calloutCdrEntity.getMainCdr();
                Long hangUpTime = mainCdr.getHangUpTime();
                CallParticipantEnum hangingUpReason;
                if (Objects.isNull(hangUpTime) || hangUpTime == 0) {
                    //说明是坐席的挂断消息先到
                    hangingUpReason = CallParticipantEnum.AGENT;
                } else {
                    hangingUpReason = CallParticipantEnum.CUSTOMER;
                }
                calloutCdrEntity.setHangingReason(hangingUpReason);
                CallOutResultEnum callResult = calloutCdrEntity.getCallResult();
                if (Objects.isNull(callResult)) {
                    calloutCdrEntity.setCallOperateResult(OperationStatusEnum.OTHER_FAIL);
                }
                mainCdr.setHangingReason(hangingUpReason);
                mainCdr = mainCdrRepository.save(mainCdr);
                calloutCdrEntity.setMainCdr(mainCdr);
                calloutCdrEntity = calloutCdrRepository.save(calloutCdrEntity);
                agentAccount = calloutCdrEntity.getAccount();
                break;
            case monitor:
                MonitorLogEntity monitorLogEntity = monitorLogRepository.findOne(callerCallId);
                monitorLogEntity.setBeginTime(triggerTime);
                monitorLogEntity = monitorLogRepository.save(monitorLogEntity);
                mainCdr = monitorLogEntity.getMainCdr();
                agentAccount = monitorLogEntity.getAccount();
                break;
            case callin:
                break;
            default:
        }
        //发布 redis 坐席挂断事件
        AgentRingEventRequest model = AgentRingEventRequest.builder()
                .account(agentAccount)
                .calledCallId(mainCdr.getId())
                .callerCallId(callerCallId)
                .triggerTime(triggerTime)
                .callEventTypeEnum(callEventTypeEnum)
                .build();
        callAbilityPublishService.agentHangup(model);
    }

    /**
     * <p>坐席接听推送事件逻辑处理</p>
     *
     * @param request 坐席推送事件
     * @author panliyong  2019-11-19 17:46
     */
    private void processAgentAnswer(Fs2CaTopic.AgentAnswer request) {
        String callerCallId = request.getCallId();
        long triggerTime = request.getTriggerTime();

        Fs2CaTopic.CallType callType = request.getCallType();
        CallEventTypeEnum callEventTypeEnum = CallEventTypeEnum.CALL_OUT;
        switch (callType) {
            case transfer:
                callEventTypeEnum = CallEventTypeEnum.TRANSFER;
                break;
            case callin:
                callEventTypeEnum = CallEventTypeEnum.CALL_IN;
                break;
            case monitor:
                callEventTypeEnum = CallEventTypeEnum.MONITOR;
                break;
            case callout:
                callEventTypeEnum = CallEventTypeEnum.CALL_OUT;
                break;
            default:
        }
        MainCdrEntity mainCdr = null;
        String agentAccount = "";
        switch (callType) {
            case transfer:
            case callout:
                CalloutCdrEntity calloutCdrEntity = calloutCdrRepository.findOne(callerCallId);
                calloutCdrEntity.setConnectTime(triggerTime);
                Long ringTime = calloutCdrEntity.getRingTime();
                if (Objects.isNull(ringTime) || ringTime == 0) {
                    ringTime = triggerTime;
                }
                calloutCdrEntity.setRingDuration(triggerTime - ringTime);
                calloutCdrEntity.setCallResult(CallOutResultEnum.connected);
                calloutCdrEntity = calloutCdrRepository.save(calloutCdrEntity);
                mainCdr = calloutCdrEntity.getMainCdr();
                break;
            case monitor:
                MonitorLogEntity monitorLogEntity = monitorLogRepository.findOne(callerCallId);
                monitorLogEntity.setBeginTime(triggerTime);
                monitorLogEntity = monitorLogRepository.save(monitorLogEntity);
                mainCdr = monitorLogEntity.getMainCdr();
                agentAccount = monitorLogEntity.getAccount();
                break;
            case callin:
                break;
            default:
        }

        //发布 redis 坐席接听事件
        assert mainCdr != null;
        AgentAnswerEventRequest model = AgentAnswerEventRequest.builder()
                .account(agentAccount)
                .calledCallId(mainCdr.getId())
                .callerCallId(callerCallId)
                .triggerTime(triggerTime)
                .callEventTypeEnum(callEventTypeEnum)
                .build();
        callAbilityPublishService.agentAnswer(model);
    }

    /**
     * <p>坐席响铃事件推送处理</p>
     *
     * @param request 坐席响铃事件
     * @author panliyong  2019-11-19 16:43
     */
    private void processAgentRing(Fs2CaTopic.AgentRing request) {
        String callerCallId = request.getCallId();
        long triggerTime = request.getTriggerTime();
        Fs2CaTopic.CallType callType = request.getCallType();
        CallEventTypeEnum callEventTypeEnum = CallEventTypeEnum.CALL_OUT;
        switch (callType) {
            case transfer:
                callEventTypeEnum = CallEventTypeEnum.TRANSFER;
                break;
            case callin:
                callEventTypeEnum = CallEventTypeEnum.CALL_IN;
                break;
            case monitor:
                callEventTypeEnum = CallEventTypeEnum.MONITOR;
                break;
            case callout:
                callEventTypeEnum = CallEventTypeEnum.CALL_OUT;
                break;
            default:
        }
        MainCdrEntity mainCdr = null;
        String agentAccount = "";
        switch (callType) {
            case transfer:
                //    TODO 呼入的转接咋办
            case callout:
                CalloutCdrEntity calloutCdrEntity = calloutCdrRepository.findOne(callerCallId);
                calloutCdrEntity.setRingTime(triggerTime);
                calloutCdrEntity = calloutCdrRepository.save(calloutCdrEntity);
                mainCdr = calloutCdrEntity.getMainCdr();
                agentAccount = calloutCdrEntity.getAccount();
                break;
            case monitor:
                MonitorLogEntity monitorLogEntity = monitorLogRepository.findOne(callerCallId);
                mainCdr = monitorLogEntity.getMainCdr();
                agentAccount = monitorLogEntity.getAccount();
                break;
            case callin:
                break;
            default:
        }
        //发布 redis 坐席响铃事件
        assert mainCdr != null;
        AgentRingEventRequest model = AgentRingEventRequest.builder()
                .account(agentAccount)
                .callerCallId(callerCallId)
                .calledCallId(mainCdr.getId())
                .triggerTime(triggerTime)
                .province(mainCdr.getProvince())
                .city(mainCdr.getCity())
                .operator(mainCdr.getOperator())
                .callEventTypeEnum(callEventTypeEnum)
                .build();
        callAbilityPublishService.agentRing(model);
    }

    /**
     * <p>坐席挂断回复事件处理</p>
     *
     * @param response 坐席挂断回复消息
     * @author panliyong  2019-11-19 13:08
     */
    private void processAgentHangupResponse(Fs2CaTopic.AgentHangupResponse response) {
        String reqNo = response.getReqNo();
        int resultCode = response.getResultCode();
        long triggerTime = response.getTriggerTime();
        OperationStatusEnum operateResult = resultCode == 0 ? OperationStatusEnum.SUCCESS : OperationStatusEnum.OTHER_FAIL;
        FsRequestLogEntity requestLogEntity = fsRequestLogRepository.findOne(reqNo);
        String calledCallId = requestLogEntity.getCalledCallId();
        Ca2FsTopic.Ca2FsMessage request = requestLogEntity.getRequest();
        Ca2FsTopic.AgentHangupRequest stAgentHangupReq = request.getStAgentHangupReq();
        String account = stAgentHangupReq.getAccount();
        CallSomethingEventResponse model = CallSomethingEventResponse.builder()
                .account(account)
                .triggerTime(triggerTime)
                .calledCallId(calledCallId)
                .result(operateResult.equals(OperationStatusEnum.SUCCESS) ? OperateResultEnum.SUCCESS : OperateResultEnum.FAIL)
                .build();
        callAbilityPublishService.agentHangupRes(model);
    }

    /**
     * <p> 机器人转人工回复事件逻辑处理
     * 对应 人机协同 就是 人工介入事件
     * </p>
     *
     * @param response 机器人转人工回复事件
     * @author panliyong  2019-11-18 17:15
     */
    private void processRobot2AgentResponse(Fs2CaTopic.Robot2AgentResponse response) {
        String reqNo = response.getReqNo();
        int resultCode = response.getResultCode();
        OperationStatusEnum operateResult = resultCode == 0 ? OperationStatusEnum.SUCCESS : OperationStatusEnum.OTHER_FAIL;
        long triggerTime = response.getTriggerTime();
        FsRequestLogEntity requestLogEntity = fsRequestLogRepository.findOne(reqNo);
        String calledCallId = requestLogEntity.getCalledCallId();
        String callerCallId = requestLogEntity.getCallerCallId();
        //修改外呼记录
        CalloutCdrEntity calloutCdrEntity = calloutCdrRepository.findOne(callerCallId);

        calloutCdrEntity.setCallOperateResult(operateResult);
        switch (operateResult) {
            case SUCCESS:
                calloutCdrEntity.setCallTime(triggerTime);
                break;
            case OTHER_FAIL:
                break;
            default:
        }
        calloutCdrRepository.save(calloutCdrEntity);
        //redis 发布人工介入回复消息
        Ca2FsTopic.Ca2FsMessage request = requestLogEntity.getRequest();
        Ca2FsTopic.Robot2AgentRequest stRobot2AgentReq = request.getStRobot2AgentReq();
        String account = stRobot2AgentReq.getAccount();
        CallSomethingEventResponse model = CallSomethingEventResponse.builder()
                .account(account)
                .triggerTime(triggerTime)
                .calledCallId(calledCallId)
                .result(operateResult.equals(OperationStatusEnum.SUCCESS) ? OperateResultEnum.SUCCESS : OperateResultEnum.FAIL)
                .build();

        callAbilityPublishService.robot2AgentRes(model);
    }

    /**
     * <p>转回机器人回复消息逻辑处理</p>
     *
     * @param response 转回机器人回复消息
     * @author panliyong  2019-11-18 19:41
     */
    private void processAgent2RobotResponse(Fs2CaTopic.Agent2RobotResponse response) {
        String reqNo = response.getReqNo();
        int resultCode = response.getResultCode();
        OperationStatusEnum operateResult = resultCode == 0 ? OperationStatusEnum.SUCCESS : OperationStatusEnum.OTHER_FAIL;
        long triggerTime = response.getTriggerTime();
        FsRequestLogEntity requestLogEntity = fsRequestLogRepository.findOne(reqNo);
        String calledCallId = requestLogEntity.getCalledCallId();
        String callerCallId = requestLogEntity.getCallerCallId();

        //修改外呼记录
        CalloutCdrEntity calloutCdrEntity = calloutCdrRepository.findOne(callerCallId);
        calloutCdrEntity.setCallOperateResult(operateResult);
        switch (operateResult) {
            case SUCCESS:
                calloutCdrEntity.setCallTime(triggerTime);
                break;
            case OTHER_FAIL:
                break;
            default:
        }
        calloutCdrRepository.save(calloutCdrEntity);
        //redis 发布人工介入回复消息
        Ca2FsTopic.Ca2FsMessage request = requestLogEntity.getRequest();
        Ca2FsTopic.Agent2RobotRequest stAgent2RobotReq = request.getStAgent2RobotReq();
        Agent2RobotEventResponse model = Agent2RobotEventResponse.builder()
                .triggerTime(triggerTime)
                .calledCallId(calledCallId)
                .callerCallId(callerCallId)
                .nodeId(stAgent2RobotReq.getNodeId())
                .robotId(stAgent2RobotReq.getRobotId())
                .sceneId(stAgent2RobotReq.getSceneId())
                .result(operateResult.equals(OperationStatusEnum.SUCCESS) ? OperateResultEnum.SUCCESS : OperateResultEnum.FAIL)
                .build();
        callAbilityPublishService.agent2RobotRes(model);
    }

    /**
     * <p>坐席转接坐席回复消息逻辑处理</p>
     *
     * @param response 坐席转接坐席回复消息
     * @author panliyong  2019-11-18 19:42
     */
    private void processAgent2AgentResponse(Fs2CaTopic.Agent2AgentResponse response) {
        String reqNo = response.getReqNo();
        int resultCode = response.getResultCode();
        OperationStatusEnum operateResult = resultCode == 0 ? OperationStatusEnum.SUCCESS : OperationStatusEnum.OTHER_FAIL;
        long triggerTime = response.getTriggerTime();
        FsRequestLogEntity requestLogEntity = fsRequestLogRepository.findOne(reqNo);
        String callerCallId = requestLogEntity.getCallerCallId();
        String calledCallId = requestLogEntity.getCalledCallId();
        //修改外呼记录
        CalloutCdrEntity calloutCdrEntity = calloutCdrRepository.findOne(callerCallId);
        calloutCdrEntity.setCallOperateResult(operateResult);
        switch (operateResult) {
            case SUCCESS:
                calloutCdrEntity.setCallTime(triggerTime);
                calloutCdrEntity.setCallType(CallTypeEnum.TRANSFER);
                break;
            case OTHER_FAIL:
                break;
            default:
        }
        calloutCdrRepository.save(calloutCdrEntity);
        //redis 发布人工介入回复消息
        Ca2FsTopic.Ca2FsMessage request = requestLogEntity.getRequest();
        Ca2FsTopic.Agent2AgentRequest stRobot2AgentReq = request.getStAgent2AgentReq();
        String account = stRobot2AgentReq.getTransferedAccount();
        CallSomethingEventResponse model = CallSomethingEventResponse.builder()
                .account(account)
                .calledCallId(calledCallId)
                .triggerTime(triggerTime)
                .result(operateResult.equals(OperationStatusEnum.SUCCESS) ? OperateResultEnum.SUCCESS : OperateResultEnum.FAIL)
                .build();
        callAbilityPublishService.agent2AgentRes(model);
    }

    /**
     * <p>取消保持回复消息处理</p>
     *
     * @param response 取消保持回复消息
     * @author panliyong  2019-11-18 9:23
     */
    private void processUnHoldResponse(Fs2CaTopic.UnHoldResponse response) {
        String reqNo = response.getReqNo();
        int resultCode = response.getResultCode();
        OperateResultEnum operateResult = resultCode == 0 ? OperateResultEnum.SUCCESS : OperateResultEnum.FAIL;
        long triggerTime = response.getTriggerTime();
        FsRequestLogEntity requestLogEntity = fsRequestLogRepository.findOne(reqNo);
        Ca2FsTopic.Ca2FsMessage request = requestLogEntity.getRequest();
        Ca2FsTopic.UnHoldRequest stUnHoldReq = request.getStUnHoldReq();
        String account = stUnHoldReq.getAccount();
        String calledCallId = requestLogEntity.getCalledCallId();
        //修改监听记录
        HoldLogEntity holdLogEntity = holdLogDao.findByCalledCallIdAndAccount(calledCallId, account);
        holdLogEntity.setEndTime(triggerTime);
        long duration = triggerTime - holdLogEntity.getBeginTime();
        holdLogEntity.setDuration(Math.max(duration, 0));
        holdLogRepository.save(holdLogEntity);
        //发送redis订阅
        CallSomethingEventResponse model = CallSomethingEventResponse.builder()
                .account(account)
                .calledCallId(calledCallId)
                .triggerTime(triggerTime)
                .result(operateResult)
                .build();
        callAbilityPublishService.unHoldRes(model);
    }

    /**
     * <p>保持回复消息处理</p>
     *
     * @param response 保持回复消息
     * @author panliyong  2019-11-18 9:23
     */
    private void processHoldResponse(Fs2CaTopic.HoldResponse response) {
        String reqNo = response.getReqNo();
        int resultCode = response.getResultCode();
        OperateResultEnum operateResult = resultCode == 0 ? OperateResultEnum.SUCCESS : OperateResultEnum.FAIL;
        long triggerTime = response.getTriggerTime();
        FsRequestLogEntity requestLogEntity = fsRequestLogRepository.findOne(reqNo);
        String calledCallId = requestLogEntity.getCalledCallId();
        //修改监听记录
        String monitorLogId = requestLogEntity.getExtended();
        HoldLogEntity holdLogEntity = holdLogRepository.findOne(monitorLogId);
        holdLogEntity.setOperateResult(operateResult);
        switch (operateResult) {
            case SUCCESS:
                holdLogEntity.setBeginTime(triggerTime);
                break;
            case FAIL:
                holdLogEntity.setBeginTime(0L);
                break;
            default:
        }
        holdLogRepository.save(holdLogEntity);

        Ca2FsTopic.Ca2FsMessage request = requestLogEntity.getRequest();
        Ca2FsTopic.HoldRequest stHoldReq = request.getStHoldReq();
        String account = stHoldReq.getAccount();
        CallSomethingEventResponse model = CallSomethingEventResponse.builder()
                .account(account)
                .calledCallId(calledCallId)
                .triggerTime(triggerTime)
                .result(operateResult)
                .build();
        callAbilityPublishService.holdRes(model);
    }

    /**
     * <p>取消监听回复消息处理</p>
     *
     * @param response 取消监听回复消息
     * @author panliyong  2019-11-18 9:17
     */
    private void processUnMonitorResponse(Fs2CaTopic.UnMonitorResponse response) {
        String reqNo = response.getReqNo();
        int resultCode = response.getResultCode();
        OperateResultEnum operateResult = resultCode == 0 ? OperateResultEnum.SUCCESS : OperateResultEnum.FAIL;
        long triggerTime = response.getTriggerTime();
        FsRequestLogEntity requestLogEntity = fsRequestLogRepository.findOne(reqNo);
        Ca2FsTopic.Ca2FsMessage request = requestLogEntity.getRequest();
        String calledCallId = requestLogEntity.getCalledCallId();
        Ca2FsTopic.UnMonitorRequest stUnMonitorReq = request.getStUnMonitorReq();
        String account = stUnMonitorReq.getAccount();

        //修改监听记录
        MonitorLogEntity monitorLogEntity = monitorLogDao.findByCalledCallIdAndAccount(calledCallId, account);
        monitorLogEntity.setEndTime(triggerTime);
        long duration = triggerTime - monitorLogEntity.getBeginTime();
        monitorLogEntity.setDuration(Math.max(duration, 0));
        monitorLogRepository.save(monitorLogEntity);

        CallSomethingEventResponse model = CallSomethingEventResponse.builder()
                .account(account)
                .calledCallId(calledCallId)
                .triggerTime(triggerTime)
                .result(operateResult)
                .build();
        callAbilityPublishService.unMonitorRes(model);
    }

    /**
     * <p>监听回复消息处理</p>
     *
     * @param response 监听回复消息
     * @author panliyong  2019-11-18 9:17
     */
    private void processMonitorResponse(Fs2CaTopic.MonitorResponse response) {
        String reqNo = response.getReqNo();
        int resultCode = response.getResultCode();
        OperateResultEnum operateResult = resultCode == 0 ? OperateResultEnum.SUCCESS : OperateResultEnum.FAIL;
        long triggerTime = response.getTriggerTime();
        FsRequestLogEntity requestLogEntity = fsRequestLogRepository.findOne(reqNo);
        String calledCallId = requestLogEntity.getCalledCallId();
        //修改监听记录
        String monitorLogId = requestLogEntity.getExtended();
        MonitorLogEntity monitorLogEntity = monitorLogRepository.findOne(monitorLogId);
        monitorLogEntity.setOperateResult(operateResult);
        switch (operateResult) {
            case SUCCESS:
                monitorLogEntity.setBeginTime(triggerTime);
                break;
            case FAIL:
                monitorLogEntity.setBeginTime(0L);
                break;
            default:
        }
        monitorLogRepository.save(monitorLogEntity);
        //redis 发布监听回复消息
        Ca2FsTopic.Ca2FsMessage request = requestLogEntity.getRequest();
        Ca2FsTopic.MonitorRequest stMonitorReq = request.getStMonitorReq();
        String account = stMonitorReq.getAccount();
        CallSomethingEventResponse model = CallSomethingEventResponse.builder()
                .account(account)
                .calledCallId(calledCallId)
                .triggerTime(triggerTime)
                .result(operateResult)
                .build();
        callAbilityPublishService.monitorRes(model);
    }

    /**
     * <p>机器人 发起呼叫回复消息处理</p>
     *
     * @param response 机器人发起呼叫回复消息
     * @author panliyong  2019-11-18 17:05
     */
    private void processRobotCallOutResponse(Fs2CaTopic.RobotCallOutResponse response) {
        String reqNo = response.getReqNo();
        Fs2CaTopic.OperateResult resultCode = response.getOperateResult();
        OperationStatusEnum operationStatusResult = resultCode.equals(Fs2CaTopic.OperateResult.succeed)
                ? OperationStatusEnum.SUCCESS
                : OperationStatusEnum.OTHER_FAIL;
        long callTime = response.getCallTime();
        long hangupTime = response.getHangupTime();
        FsRequestLogEntity requestLogEntity = fsRequestLogRepository.findOne(reqNo);
        String calledCallId = requestLogEntity.getCalledCallId();
        String callerCallId = requestLogEntity.getCallerCallId();

        CalloutCdrEntity calloutCdrEntity = calloutCdrRepository.findOne(callerCallId);
        CallParticipantEnum callerRole = calloutCdrEntity.getCallerRole();
        //    先修改对应的mainCDR的数据
        MainCdrEntity mainCdrEntity = mainCdrRepository.findOne(calledCallId);
        mainCdrEntity.setCallOperateResult(operationStatusResult);
        mainCdrEntity.setStartingTime(callTime);
        switch (operationStatusResult) {
            case OTHER_FAIL:
                mainCdrEntity.setCallDuration(0L);
                mainCdrEntity.setHangingReason(callerRole);
                break;
            case SUCCESS:
                break;
            default:
        }
        mainCdrRepository.save(mainCdrEntity);
        //修改外呼CDR的数据
        calloutCdrEntity.setCallOperateResult(operationStatusResult);
        switch (operationStatusResult) {
            case OTHER_FAIL:
                calloutCdrEntity.setCallDuration(0L);
                calloutCdrEntity.setHangingReason(callerRole);
                break;
            case SUCCESS:
                calloutCdrEntity.setCallTime(callTime);
                break;
            default:
        }
        calloutCdrRepository.save(calloutCdrEntity);

        //    发布redis 订阅事件
        CallEventResponse model = CallEventResponse.builder()
                .operateResult(operationStatusResult)
                .account(calloutCdrEntity.getAccount())
                .called(mainCdrEntity.getCalled())
                .calledCallId(calledCallId)
                .callerCallId(callerCallId)
                .callTime(callTime)
                .hangupTime(hangupTime)
                .build();
        callAbilityPublishService.callRes(model);
    }

    /**
     * <p>坐席发起呼叫回复消息处理</p>
     *
     * @param response 坐席发起呼叫回复消息
     * @author panliyong  2019-11-18 16:10
     */
    private void processAgentCallOutResponse(Fs2CaTopic.AgentCallOutResponse response) {
        String reqNo = response.getReqNo();
        Fs2CaTopic.OperateResult resultCode = response.getOperateResult();
        OperationStatusEnum operationStatusResult = resultCode.equals(Fs2CaTopic.OperateResult.succeed)
                ? OperationStatusEnum.SUCCESS
                : OperationStatusEnum.OTHER_FAIL;
        long callTime = response.getCallTime();
        long hangupTime = response.getHangupTime();
        FsRequestLogEntity requestLogEntity = fsRequestLogRepository.findOne(reqNo);
        String calledCallId = requestLogEntity.getCalledCallId();
        String callerCallId = requestLogEntity.getCallerCallId();

        CalloutCdrEntity calloutCdrEntity = calloutCdrRepository.findOne(callerCallId);
        CallParticipantEnum callerRole = calloutCdrEntity.getCallerRole();
        //    先修改对应的mainCDR的数据
        MainCdrEntity mainCdrEntity = mainCdrRepository.findOne(calledCallId);
        mainCdrEntity.setCallOperateResult(operationStatusResult);
        mainCdrEntity.setStartingTime(callTime);
        switch (operationStatusResult) {
            case OTHER_FAIL:
                mainCdrEntity.setCallDuration(0L);
                mainCdrEntity.setHangingReason(callerRole);
                break;
            case SUCCESS:
                break;
            default:
        }
        mainCdrRepository.save(mainCdrEntity);
        //    修改 callout cdr 信息
        calloutCdrEntity.setCallOperateResult(operationStatusResult);
        switch (operationStatusResult) {
            case OTHER_FAIL:
                calloutCdrEntity.setCallDuration(0L);
                calloutCdrEntity.setHangingReason(callerRole);
                break;
            case SUCCESS:
                calloutCdrEntity.setCallTime(callTime);
                break;
            default:
        }
        calloutCdrRepository.save(calloutCdrEntity);

        //    发布redis 订阅事件
        CallEventResponse model = CallEventResponse.builder()
                .operateResult(operationStatusResult)
                .account(calloutCdrEntity.getAccount())
                .called(mainCdrEntity.getCalled())
                .calledCallId(calledCallId)
                .callerCallId(callerCallId)
                .callTime(callTime)
                .hangupTime(hangupTime)
                .build();
        callAbilityPublishService.callRes(model);
    }

    /**
     * <p>机器人转外部</p>
     *
     * @param response 机器人转外部返回消息
     * @author panliyong  2019-11-25 11:54
     */
    private void processRobot2OutResponse(Fs2CaTopic.Robot2OutResponse response) {
        String reqNo = response.getReqNo();
        int resultCode = response.getResultCode();
        OperateResultEnum operateResult = resultCode == 0 ? OperateResultEnum.SUCCESS : OperateResultEnum.FAIL;
        FsRequestLogEntity requestLogEntity = fsRequestLogRepository.findOne(reqNo);
        String calledCallId = requestLogEntity.getCalledCallId();
        RobotInfoEntity robotInfoEntity = robotInfoRepository.findByMainCdrId(calledCallId);
        robotInfoEntity.setTransferOutOperateResult(operateResult);
        robotInfoRepository.save(robotInfoEntity);
    }

    /**
     * <p>签出消息回复处理</p>
     *
     * @param response 签出回复消息
     * @author panliyong  2019-11-18 9:09
     */
    private void processCheckOutResponse(Fs2CaTopic.CheckOutResponse response) {
        String reqNo = response.getReqNo();
        int resultCode = response.getResultCode();
        OperateResultEnum operateResult = resultCode == 0 ? OperateResultEnum.SUCCESS : OperateResultEnum.FAIL;
        long triggerTime = response.getTriggerTime();
        FsRequestLogEntity requestLogEntity = fsRequestLogRepository.findOne(reqNo);
        Ca2FsTopic.Ca2FsMessage request = requestLogEntity.getRequest();
        Ca2FsTopic.CheckOutRequest checkOutReq = request.getStCheckOutReq();
        String account = checkOutReq.getAccount();
        EventOperateResponse model = EventOperateResponse.builder()
                .account(account)
                .triggerTime(triggerTime)
                .result(operateResult)
                .build();
        callAbilityPublishService.checkOutRes(model);
    }

    /**
     * <p>签入消息回复处理</p>
     *
     * @param response 签入回复消息
     * @author panliyong  2019-11-16 17:09
     */
    private void processCheckInResponse(Fs2CaTopic.CheckInResponse response) {
        String reqNo = response.getReqNo();
        int resultCode = response.getResultCode();
        OperateResultEnum operateResult = resultCode == 0 ? OperateResultEnum.SUCCESS : OperateResultEnum.FAIL;
        long triggerTime = response.getTriggerTime();
        FsRequestLogEntity requestLogEntity = fsRequestLogRepository.findOne(reqNo);
        if (Objects.isNull(requestLogEntity)) {
            log.error("【Fs2CaConsumerHandler】->processCheckInResponse 消息返回的reqNo不正确 =【{}】", reqNo);
            return;
        }
        Ca2FsTopic.Ca2FsMessage request = requestLogEntity.getRequest();
        Ca2FsTopic.CheckInRequest stCheckInReq = request.getStCheckInReq();
        String account = stCheckInReq.getAccount();
        EventOperateResponse model = EventOperateResponse.builder()
                .account(account)
                .triggerTime(triggerTime)
                .result(operateResult)
                .build();
        callAbilityPublishService.checkInRes(model);
    }
}
