package com.bokmcdok.wheat.ai.tasks;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.util.WeightedList;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ModMultiTask<E extends LivingEntity> extends Task<E> {
    private final Set<MemoryModuleType<?>> mMemoryModuleTypes;
    private final Ordering mOrdering;
    private final RunType mRunType;
    private final WeightedList<Task<? super E>> mTasks = new WeightedList<>();

    /**
     * Construction
     * @param memoryModules List of memory modules required by this task.
     * @param memoryModuleTypes List of memory modules used by tasks in this multitask.
     * @param ordering The ordering of the tasks.
     * @param runType The run type - one at a time or try all.
     * @param priorities List of tasks in this multitask and their priorities.
     */
    public ModMultiTask(Map<MemoryModuleType<?>, MemoryModuleStatus> memoryModules, Set<MemoryModuleType<?>> memoryModuleTypes, Ordering ordering, RunType runType, List<Pair<Task<? super E>, Integer>> priorities) {
        super(memoryModules);
        mMemoryModuleTypes = memoryModuleTypes;
        mOrdering = ordering;
        mRunType = runType;
        priorities.forEach((x) -> {
            mTasks.func_226313_a_(x.getFirst(), x.getSecond());
        });
    }

    /**
     * Continue executing if any tasks in this multitask still need executing.
     * @param world The current world.
     * @param entity The entity.
     * @param gameTime The current game time.
     * @return TRUE if this task should continue executing.
     */
    @Override
    protected boolean shouldContinueExecuting(ServerWorld world, E entity, long gameTime) {
        return mTasks.func_220655_b().filter((x) -> x.getStatus() == Status.RUNNING)
                .anyMatch((task) -> {
                    try {
                        Method shouldContinueExecuting = ObfuscationReflectionHelper.findMethod(Task.class, "shouldContinueExecuting", ServerWorld.class, LivingEntity.class, long.class);
                        return (boolean)shouldContinueExecuting.invoke(task, world, entity, gameTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                });
    }

    /**
     * Multitasks never time out.
     * @param gameTime The current game time.
     * @return Always FALSE.
     */
    @Override
    protected boolean isTimedOut(long gameTime) {
        return false;
    }

    /**
     * Start executing tasks.
     * @param world The current world.
     * @param entity The entity.
     * @param gameTime The current game time.
     */
    @Override
    protected void startExecuting(ServerWorld world, E entity, long gameTime) {
        mOrdering.accept(mTasks);
        mRunType.run(mTasks, world, entity, gameTime);
    }

    /**
     * Update all executing tasks.
     * @param world The current world.
     * @param entity The entity.
     * @param gameTime The current game time.
     */
    @Override
    protected void updateTask(ServerWorld world, E entity, long gameTime) {
        mTasks.func_220655_b().filter((x) -> x.getStatus() == Status.RUNNING).forEach((x) -> {
            x.tick(world, entity, gameTime);
        });
    }

    /**
     * Reset all tasks.
     * @param world The current world.
     * @param entity The entity.
     * @param gameTime The current game time.
     */
    @Override
    protected void resetTask(ServerWorld world, E entity, long gameTime) {
        mTasks.func_220655_b().filter((x) -> x.getStatus() == Status.RUNNING).forEach((x) -> {
            x.stop(world, entity, gameTime);
        });
        mMemoryModuleTypes.forEach(entity.getBrain()::removeMemory);
    }

    /**
     * Convert the task to a string.
     * @return A string representation of the multitask.
     */
    @Override
    public String toString() {
        Set<? extends Task<? super E>> set = mTasks.func_220655_b().filter((x) -> x.getStatus() == Status.RUNNING).collect(Collectors.toSet());
        return "(" + getClass().getSimpleName() + "): " + set;
    }

    /**
     * Task ordering - ordered or shuffled.
     */
    public enum Ordering {
        ORDERED((p_220627_0_) -> {
        }),
        SHUFFLED(WeightedList::func_226309_a_);

        private final Consumer<WeightedList<?>> mTasks;

        Ordering(Consumer<WeightedList<?>> tasks) {
            mTasks = tasks;
        }

        public void accept(WeightedList<?> tasks) {
            mTasks.accept(tasks);
        }
    }

    /**
     * Task Run Type - Run one or try all.
     */
    public enum RunType {
        RUN_ONE {
            public <E extends LivingEntity> void run(WeightedList<Task<? super E>> tasks, ServerWorld world, E entity, long gameTime) {
                tasks.func_220655_b().filter((x) -> x.getStatus() == Status.STOPPED).filter((x) -> x.start(world, entity, gameTime)).findFirst();
            }
        },
        TRY_ALL {
            public <E extends LivingEntity> void run(WeightedList<Task<? super E>> tasks, ServerWorld world, E entity, long gameTime) {
                tasks.func_220655_b().filter((x) -> x.getStatus() == Status.STOPPED).forEach((x) -> {
                    x.start(world, entity, gameTime);
                });
            }
        };

        RunType() {
        }

        public abstract <E extends LivingEntity> void run(WeightedList<Task<? super E>> p_220630_1_, ServerWorld p_220630_2_, E p_220630_3_, long p_220630_4_);
    }
}