package jp.co.yanagawa.bachTemplete.task;

import jp.co.yanagawa.bachTemplete.common.Cipher;
import jp.co.yanagawa.bachTemplete.common.Config;
import jp.co.yanagawa.bachTemplete.common.Constants;
import jp.co.yanagawa.bachTemplete.dao.AudienceIdEncryptionDao;
import jp.co.yanagawa.bachTemplete.entity.AudienceIdEncryption;
import jp.co.yanagawa.bachTemplete.exception.CipherException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static jp.co.yanagawa.bachTemplete.util.FileUtil.fileFilterEndsWith;
import static jp.co.yanagawa.bachTemplete.util.FileUtil.fileMove;

public class  NewTask implements Task {



}