package pl.mrjozvvicki.nexusguilds.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import pl.mrjozvvicki.nexusguilds.NexusGuilds;
import pl.mrjozvvicki.nexusguilds.entities.Guild;
import pl.mrjozvvicki.nexusguilds.entities.Nexus;
import pl.mrjozvvicki.nexusguilds.tasks.AcceptDeclineTask;
import pl.mrjozvvicki.nexusguilds.utils.Chat;
import pl.mrjozvvicki.nexusguilds.utils.ItemsChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommandManager {
    static CommandManager instance;
    static GuildsManager guildsManager;
    static ItemsChecker itemsChecker;
    public static List<String> mainCommands = List.of("help", "info", "accept", "decline", "new", "del", "list", "nexus", "member", "transfer");
    public static List<String> nexusCommands = List.of("create", "upgrade", "pos", "tp");
    public static List<String> memberCommands = List.of("list", "add", "remove");

    public static NexusGuilds plugin;
    static Map<String, BukkitTask> tasks = new HashMap<>();
    static Map<String, String> guildsToCreate = new HashMap<>();

    public CommandManager(NexusGuilds plugin) {
        CommandManager.plugin = plugin;
        guildsManager = GuildsManager.getInstance();
        itemsChecker = ItemsChecker.getInstance();
    }

    public static CommandManager getInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new CommandManager((NexusGuilds) plugin);
        }

        return instance;
    }

    public List<String> getCommands(String[] args) {
        if (args.length == 1) {
            return mainCommands;
        }
        return List.of();
    }

    public List<String> getNexusCommands(String[] args) {
        if (args.length == 2) {
            return nexusCommands;
        }
        return List.of();
    }

    public List<String> getMemberCommands(String[] args) {
        if (args.length == 2) {
            return memberCommands;
        }
        return List.of();
    }

    public boolean handleCommand(Player p, String[] args) {
        if (args.length >= 1) {
            String commandType = args[0];
            return switch (commandType) {
                case "new" -> handleNewCommand(p, args);
                case "del" -> handleDelCommand(p, args);
                case "list" -> handleListCommand(p, args);
                case "nexus" -> handleNexusCommand(p, args);
                case "info" -> handleInfoCommand(p, args);
                case "member" -> handleMemberCommand(p, args);
                case "transfer" -> handleTransferCommand(p, args);
                case "accept" -> handleAcceptCommand(p, args);
                case "decline" -> handleDeclineCommand(p, args);
                default -> handleHelpCommand(p, args);
            };
        }

        return handleHelpCommand(p, args);
    }

    private static boolean checkGuildNameExists(String arg) {
        return arg != null && !arg.isEmpty();
    }

    private static boolean handleNewCommand(Player p, String[] args) {
        if (checkGuildNameExists(args[1])) {
            List<Guild> guilds = guildsManager.getGuilds();

            for (Guild guild : guilds) {
                if (Objects.equals(guild.getLeader(), p.getName())) {
                    Chat.sendMessage(p, "&7Jesteś już liderem gildii!");
                    return false;
                }

                if (Objects.equals(guild.getName(), args[1])) {
                    Chat.sendMessage(p, "&7Gildia &4" + guild.getName() + "&7 już istnieje");
                    return false;
                }
            }

            if (!ItemsChecker.hasItems(p)) {
                Chat.sendMessage(p, "&7Nie posiadasz wystarczającej ilości przedmiotów, by postawić nexus!");
                return false;
            }

            Chat.sendMessage(p, "&7Utworzenie gildii wymaga twojej akceptacji!");
            Chat.sendMessage(p, "&7Na akceptację masz &430 &7sekund!");
            Chat.sendMessage(p, "&7Użyj &4/mg accept&7, aby utworzyć gildię!");
            Chat.sendMessage(p, "&7Użyj &4/mg decline&7, aby odrzucić tworzenie!");

            tasks.put(p.getName(), new AcceptDeclineTask(p).runTaskTimer(plugin, 0, 30));
            guildsToCreate.put(p.getName(), args[1]);

            return true;
        }

        Chat.sendMessage(p, "&7Nie podano nazwy gildii!");
        return false;
    }

    public static boolean handleAcceptCommand(Player p, String[] args) {
        if (tasks.get(p.getName()) != null) {
            String guildName = guildsToCreate.get(p.getName());
            if (guildName == null) return false;

            Guild newGuild = new Guild(guildName, p.getName());
            guildsManager.addGuild(newGuild);
            guildsManager.createNexus(p, newGuild);
            ItemsChecker.removeItems(p);
            Chat.sendMessage(p, "&7Gildia &4" + newGuild.getName() + "&7 została utworzona!");
            guildsToCreate.remove(p.getName());
            tasks.get(p.getName()).cancel();
            tasks.remove(p.getName());
            return true;
        }

        return false;
    }

    public static boolean handleDeclineCommand(Player p, String[] args) {
        Chat.sendMessage(p, "&7Tworzenie gildii zostało anulowane!");
        guildsToCreate.remove(p.getName());
        tasks.get(p.getName()).cancel();
        tasks.remove(p.getName());
        return false;
    }

    private static boolean handleDelCommand(Player p, String[] args) {
        Guild guild = guildsManager.findGuildByLeader(p);

        if (guild != null) {
            guild.destroyNexus();
            guildsManager.removeGuild(guild);
            Chat.sendMessage(p, "&7Gildia o nazwie &4" + guild.getName() + "&7 została usunięta!");
            return true;
        }

        Chat.sendMessage(p, "&7Nie jesteś liderem żadnej gildii!");
        return false;
    }

    private static boolean handleListCommand(Player p, String[] args) {
        List<Guild> guilds = guildsManager.getGuilds();

        if (guildsManager.getGuildsCount() > 0) {
            Chat.sendMessage(p, "&7[&4nexusguilds&7] Lista gildii:");
            int index = 1;
            for (Guild guild : guilds) {
                int guildsMembers = guild.getMembers().size();
                Chat.sendMessage(p, "&7" + index + ". " + guild.getName() + " (Lider: &4" + guild.getLeader() + "&7, Członków: &4" + guildsMembers + "&7)");
                index++;
            }
            return true;
        }

        Chat.sendMessage(p, "&7Na serwerze nie istnieje jeszcze żadna gildia!");
        return false;
    }

    private static boolean handleNexusCommand(Player p, String[] args) {
        Guild leaderGuild = guildsManager.findGuildByLeader(p);
        Guild memberGuild = guildsManager.findGuildByMember(p);
        World playerWorld = p.getWorld();

        if (memberGuild == null) {
            Chat.sendMessage(p, "&7Nie należysz do żadnej gildii");
            return false;
        }

        if (leaderGuild == null) {
            Chat.sendMessage(p, "&7Tylko właściciel gildii może zarządzać nexusem!");
            return false;
        }

        if (!playerWorld.getName().equals(Bukkit.getWorlds().get(0).getName())) {
            Chat.sendMessage(p, "&7Nexus może być utworzony tylko w normalnym świecie!");
            return false;
        }

        if (args.length == 2) {
            Nexus nexus = memberGuild.getNexus();

            if (nexus == null) {
                Chat.sendMessage(p, "&7Twoja gildia nie ma jeszcze nexusa!");
                return false;
            }

            if (args[1].equals("pos")) {
                Location nexusLoc = nexus.getLocation();
                String formattedLocation = nexusLoc.getWorld().getName() + "&7, &4" + nexusLoc.getX() + "&7, &4" + nexusLoc.getY() + "&7, &4" + nexusLoc.getZ();
                Chat.sendMessage(p, "&7Pozycja nexusa: (&4" + formattedLocation + "&7)");
                return true;
            }

            if (args[1].equals("tp")) {
                Location nexusLoc = nexus.getLocation().clone().add(-1, 0, 0);

                p.teleport(nexusLoc);
                return true;
            }
        }


        return false;
    }

    private static boolean handleHelpCommand(Player p, String[] args) {
        Chat.sendMessage(p, "&7[&4nexusguilds&7] Dostępne komendy:");
        Chat.sendMessage(p, "&4/mg new <nazwa gildii>&7 - tworzenie gildii o podanej nazwie");
        Chat.sendMessage(p, "&4/mg del&7 - usuwanie gildii, której jesteś liderem");
        Chat.sendMessage(p, "&4/mg nexus&7 - tworzenie nexusa gildii (musisz być liderem)");
        Chat.sendMessage(p, "&4/mg nexus pos&7 - wyświetla pozycję nexusa gildii");
        return false;
    }

    private static boolean handleInfoCommand(Player p, String[] args) {
        Chat.sendMessage(p, "&7[&4nexusguilds&7] Wersja: 0.1.0-SNAPSHOT");
        Chat.sendMessage(p, """
                &7Plugin dodaje gildie oparte o nexus. \
                Aby utworzyć nowy nexus lider gildii potrzebuje określonych przedmiotów:\
                &71. &48 Złotych jabłek &7(nie zaklętych)\
                &72. &432 diamentów&7\
                &73. &464 marchewki&7\
                &74. &432 skóry&7\
                Nexus tworzy region 100x100x100, w którym wrodzy gracze nie mogą otwierać ani niszczyć \
                skrzynek, pieców ani innych bloków zawierających przedmioty.\
                Zniszczenie wrogiego nexusa znosi zabezpieczenie i opóźnia odrodzenie graczy gildii, której \
                nexus został zniszczony.""");
        return false;
    }

    private static boolean handleMemberCommand(Player p, String[] args) {
        Guild guild = guildsManager.findGuildByLeader(p);

        if (guild == null) {
            Chat.sendMessage(p, "&7Nie jesteś liderem żadnej gildii!");
            return false;
        }

        if (args.length >= 2) {
            if (args.length == 3 && args[1].equals("add")) {
                if (args[2].equals(guild.getLeader())) {
                    Chat.sendMessage(p, "&7Nie możesz dodać siebie!");
                    return false;
                }

                for (Guild g : guildsManager.getGuilds()) {
                    if (g.hasMember(args[1])) {
                        Chat.sendMessage(p, "&7Gracz &4" + args[1] + "&7 należy już do innej gildii!");
                        return false;
                    }
                }

                boolean success = guild.addMember(args[2]);
                if (success) {
                    Chat.sendMessage(p, "&7Dodano nowego członka!");
                    return true;
                }

                Chat.sendMessage(p, "&7Podany gracz należy już do gildii!");
                return false;
            } else if (args.length == 3 && args[1].equals("remove")) {
                if (args[2].equals(guild.getLeader())) {
                    Chat.sendMessage(p, "&7Nie możesz usunąć siebie!");
                    return false;
                }

                boolean success = guild.removeMember(args[2]);
                if (success) {
                    Chat.sendMessage(p, "&7Usunięto gracza &4" + args[2] + "&7 z gildii!");
                    return true;
                } else {
                    Chat.sendMessage(p, "&7Podany gracz nie należy do twojej gildii!");
                    return false;
                }
            } else if (args.length == 2 && args[1].equals("list")) {
                Chat.sendMessage(p, "&7[&4" + guild.getName() + "&7] Lista członków:");
                int count = 1;
                for (String member : guild.getMembers()) {
                    Chat.sendMessage(p, "&7" + count + ". &7" + member);
                    count++;
                }
            }

            Chat.sendMessage(p, "&7Nie podano nazwy gracza!");
            return false;
        }

        return false;
    }

    private static boolean handleTransferCommand(Player p, String[] args) {
        Guild guild = guildsManager.findGuildByLeader(p);

        if (guild == null) {
            Chat.sendMessage(p, "&7 Nie jesteś liderem żadnej gildii!");
            return false;
        }

        List<Guild> guilds = guildsManager.getGuilds();
        if (guilds.isEmpty()) {
            Chat.sendMessage(p, "&7Na serwerze nie istnieje jeszcze ani jedna gildia!");
            return false;
        }

        if (args.length == 2) {
            for (Guild curGuild : guilds) {
                if (curGuild.getLeader().equals(args[1])) {
                    Chat.sendMessage(p, "&7Gracz &4" + args[1] + "&7 jest już liderem gildii " + guild.getName() + "&7!");
                    return false;
                }
            }

            Chat.sendMessage(p, "&7Zmieniono lidera gildii &4" + guild.getName() + "&7 na gracza &4" + args[1] + "&7!");
            guild.changeLeader(args[1]);
            return true;
        }

        return false;
    }
}
