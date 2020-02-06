package io.jingwei.base.utils.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

@SuppressWarnings("Duplicates")
public class IpUtil {
    private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);
    private static final String ANYHOST_VALUE = "0.0.0.0";
    private static final String LOCALHOST_VALUE = "127.0.0.1";
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
    private static volatile InetAddress LOCAL_ADDRESS = null;
    private static final String COMPUTER_NAME = "COMPUTERNAME";

    public static String hostName;

    public IpUtil() {
    }

    static {
        hostName = getComputerHostName();
    }

    private static InetAddress toValidAddress(InetAddress address) {
        if (address instanceof Inet6Address) {
            Inet6Address v6Address = (Inet6Address)address;
            if (isPreferIPV6Address()) {
                return normalizeV6Address(v6Address);
            }
        }

        return isValidV4Address(address) ? address : null;
    }

    private static boolean isPreferIPV6Address() {
        return Boolean.getBoolean("java.net.preferIPv6Addresses");
    }

    private static boolean isValidV4Address(InetAddress address) {
        if (address != null && !address.isLoopbackAddress()) {
            String name = address.getHostAddress();
            boolean result = name != null && IP_PATTERN.matcher(name).matches() && !"0.0.0.0".equals(name) && !"127.0.0.1".equals(name);
            return result;
        } else {
            return false;
        }
    }

    private static InetAddress normalizeV6Address(Inet6Address address) {
        String addr = address.getHostAddress();
        int i = addr.lastIndexOf(37);
        if (i > 0) {
            try {
                return InetAddress.getByName(addr.substring(0, i) + '%' + address.getScopeId());
            } catch (UnknownHostException var4) {
                logger.debug("Unknown IPV6 address: ", var4);
            }
        }

        return address;
    }

    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;

        try {
            localAddress = InetAddress.getLocalHost();
            InetAddress addressItem = toValidAddress(localAddress);
            if (addressItem != null) {
                return addressItem;
            }
        } catch (Throwable var8) {
            logger.error(var8.getMessage(), var8);
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (null == interfaces) {
                return localAddress;
            }

            while(interfaces.hasMoreElements()) {
                try {
                    NetworkInterface network = interfaces.nextElement();
                    if (!network.isLoopback() && !network.isVirtual() && network.isUp()) {
                        Enumeration addresses = network.getInetAddresses();

                        while(addresses.hasMoreElements()) {
                            try {
                                InetAddress addressItem = toValidAddress((InetAddress)addresses.nextElement());
                                if (addressItem != null) {
                                    try {
                                        if (addressItem.isReachable(100)) {
                                            return addressItem;
                                        }
                                    } catch (IOException var6) {
                                    }
                                }
                            } catch (Throwable var7) {
                                logger.error(var7.getMessage(), var7);
                            }
                        }
                    }
                } catch (Throwable var9) {
                    logger.error(var9.getMessage(), var9);
                }
            }
        } catch (Throwable var10) {
            logger.error(var10.getMessage(), var10);
        }

        return localAddress;
    }

    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        } else {
            InetAddress localAddress = getLocalAddress0();
            LOCAL_ADDRESS = localAddress;
            return localAddress;
        }
    }

    public static String getIp() {
        return getLocalAddress().getHostAddress();
    }

    public static String getIpPort(int port) {
        String ip = getIp();
        return getIpPort(ip, port);
    }

    public static String getIpPort(String ip, int port) {
        return ip == null ? null : ip.concat(":").concat(String.valueOf(port));
    }

    public static Object[] parseIpPort(String address) {
        String[] array = address.split(":");
        String host = array[0];
        int port = Integer.parseInt(array[1]);
        return new Object[]{host, port};
    }

    public static String getHostNameForLinux() {
        try {
            return (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException uhe) {
            String host = uhe.getMessage(); // host = "hostname: hostname"
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }

            return "UnknownHost";
        }
    }

    /**
     * 获取host name
     */
    public static String getComputerHostName() {
        if (System.getenv(COMPUTER_NAME) != null) {
            return System.getenv(COMPUTER_NAME);
        } else {
            return getHostNameForLinux();
        }
    }

    /**
     * 获取host name
     */
    public static String getHostName() {
        return hostName;
    }

}
