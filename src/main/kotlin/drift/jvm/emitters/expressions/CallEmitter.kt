/******************************************************************************
 * Drift Programming Language                                                 *
 * Drift Backend Development: Java Virtual Machine implementation.            *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/

package drift.jvm.emitters.expressions

import drift.hir.*
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter
import drift.jvm.emitters.conventions.helpers.NamingHelper.formatClassName
import drift.jvm.emitters.types.helpers.ClassHelper.getInternalClassName
import drift.jvm.emitters.types.helpers.TypeConverter.formatTypes
import language.Namespace
import org.objectweb.asm.Opcodes.*


/**
 * This sink emitter handles call expressions.
 *
 * @author Jonathan (GitHub: belicfr)
 * @see HIRCall
 */
class CallEmitter(
    private val namespace: Namespace,
    private val context: EmitContext) : SinkEmitter<HIRCall> {

    override fun emit(node: HIRCall) {
        when (node.callee) {
            is HIRReference   -> emitFromVariableReference(node)
            is HIRMethodAccess  -> emitMethodCall(node)

            else                -> error("Unexpected callee")
        }
    }

    private fun emitFromVariableReference(node: HIRCall) {
        val callee = node.callee as HIRReference
        val calleeDefHirId = callee.definitionHirId
            ?: return

        val reference = context
            .nodesManager
            .nodesByDefinition[calleeDefHirId]
            ?: error("Unknown callee")

        when (reference) {
            is HIRClass     -> emitConstructorCall(node, reference)
            is HIRFunction  -> emitFunctionCall(node, reference)

            else            -> error("Unexpected reference")
        }
    }

    private fun emitConstructorCall(call: HIRCall, ref: HIRClass) {
        val constructor = "<init>"

        val className = getInternalClassName(
            namespace,
            ref.name)

        val ctorHook = ref
            .hooks
            .firstOrNull { it.name == "init" }
            ?: error("Invalid class structure: a class must have a constructor")
        val ctorParameterTypes = ctorHook
            .parameters
            .map { it.type }

        val ctorDescriptor = formatTypes(
            input = ctorParameterTypes,
            output = HIRPrimitiveType(PrimitiveKind.VOID))

        val isInterface = false

        with(context.methodVisitor) {
            visitTypeInsn(NEW, className)

            visitInsn(DUP)

            emitArguments(call.arguments)

            visitMethodInsn(
                INVOKESPECIAL,
                className,
                constructor,
                ctorDescriptor,
                isInterface)
        }
    }

    private fun emitFunctionCall(call: HIRCall, ref: HIRFunction) {
        val owner = formatClassName(namespace, ref.hirId, ref.name)

        val invokeFn = "invoke"

        val parameterTypes = ref
            .parameters
            .map { it.type }

        val descriptor = formatTypes(
            input = parameterTypes,
            output = ref.returnType)

        val isInterface = false

        emitArguments(call.arguments)

        context.methodVisitor.visitMethodInsn(
            INVOKESTATIC,
            owner,
            invokeFn,
            descriptor,
            isInterface)
    }

    private fun emitMethodCall(call: HIRCall) {
        /**
         * Attempt to retrieve the [HIRFunction] object from its definition
         * HIR ID.
         *
         * If the retrieved object is not a [HIRFunction], an error is thrown;
         * same if none object is found using the provided ID.
         *
         * @param defHirId The definition HIR ID of the function to retrieve.
         * @return The [HIRFunction] object.
         */
        fun getMethodFromContextOrThrow(defHirId: Int) : HIRFunction {
            return (context
                .nodesManager
                .nodesByDefinition[defHirId]
                ?: error("Undefined method in static context")) as? HIRFunction
                ?: error("Unexpected structure")
        }

        /**
         * Compute the method's descriptor by taking its parameter types
         * and return type into account.
         *
         * @param method The [HIRFunction] to compute the descriptor from.
         * @return The method's descriptor.
         */
        fun getMethodDescriptor(method: HIRFunction) : String {
            return formatTypes(
                input = method.parameters.map { it.type },
                output = method.returnType)
        }

        /**
         * Handle a static method call. Define method information,
         * push arguments on the stack, and invoke the method.
         *
         * @param callee The [HIRStaticMethodAccess] to emit from.
         */
        fun handleStaticMethod(callee: HIRStaticMethodAccess) {
            val owner = callee.receiverClassName
            val name = callee.memberName
            val method = getMethodFromContextOrThrow(callee.definitionHirId)
            val descriptor = getMethodDescriptor(method)
            val isInterface = false

            emitArguments(call.arguments)

            context.methodVisitor.visitMethodInsn(
                INVOKESTATIC,
                owner,
                name,
                descriptor,
                isInterface)
        }

        /**
         * Handle an instance method call. Emit its receiver,
         * define method information, push arguments on the stack,
         * and invoke the method.
         *
         * @param callee The [HIRInstanceMethodAccess] to emit from.
         */
        fun handleInstanceMethod(callee: HIRInstanceMethodAccess) {
            ExpressionEmitter(namespace, context)
                .emit(callee.receiver)

            val isOverridable = false
            val opcode =
                if (isOverridable) INVOKEVIRTUAL
                else INVOKESPECIAL
            val owner = callee.receiverClassName
            val name = callee.memberName
            val method = getMethodFromContextOrThrow(callee.definitionHirId)
            val descriptor = getMethodDescriptor(method)
            val isInterface = false

            emitArguments(call.arguments)

            context.methodVisitor.visitMethodInsn(
                opcode,
                owner,
                name,
                descriptor,
                isInterface)
            // NOTE: opcode should be chosen regarding method context:
            //  - overridable       => INVOKEVIRTUAL = runtime
            //  - non overridable   => INVOKESPECIAL = compile-time
        }


        when (val callee = call.callee) {
            is HIRStaticMethodAccess    -> handleStaticMethod(callee)
            is HIRInstanceMethodAccess  -> handleInstanceMethod(callee)

            else                        -> error("Unexpected callee")
        }
    }

    private fun emitArguments(args: Collection<HIRArgument>) {
        val expressionEmitter = ExpressionEmitter(namespace, context)

        for (argument in args) with(argument) {
            expressionEmitter.emit(value)
        }
    }
}